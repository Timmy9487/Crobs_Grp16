package hk.edu.polyu.comp.comp2021.crobs.model;

import static org.junit.jupiter.api.Assertions.*;

import hk.edu.polyu.comp.comp2021.crobs.manager.*;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.*;
import hk.edu.polyu.comp.comp2021.crobs.model.user.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;

public class BookingTest {
  RoomManager rooms;
  BookingManager bookings;
  RoomSearch search;
  User alice = new RegularUser("U1", "Alice"),
      bob = new RegularUser("U2", "Bob"),
      admin = new AdminUser("U0", "Admin");
  LocalDate day = LocalDate.of(2030, 1, 10);
  LocalTime ten = LocalTime.of(10, 0), eleven = LocalTime.of(11, 0), twelve = LocalTime.NOON;
  String room;

  @BeforeEach
  public void setup() throws Exception {
    rooms = new RoomManager();
    bookings = new BookingManager();
    bookings.setRoomManager(rooms);
    rooms.setBookingManager(bookings);
    room = rooms.addRoom(admin, "meetingRoom", "10", "wifi,projector").getRoomId();
    search = new RoomSearch(rooms, bookings);
  }

  Booking book() {
    return bookings.book(alice, room, day, ten, eleven, "group discussion");
  }

  @Test
  public void creationAndUniqueIds() {
    Booking b = book();
    assertEquals("U1", b.getUserId());
    assertEquals("group discussion", b.getPurpose());
    assertNotEquals(
        b.getBookingId(), bookings.book(alice, room, day, eleven, twelve, "next").getBookingId());
  }

  @Test
  public void overlappingCases() {
    book();
    for (LocalTime[] times :
        Arrays.asList(
            new LocalTime[] {ten, eleven},
            new LocalTime[] {ten.minusMinutes(1), ten.plusMinutes(1)},
            new LocalTime[] {ten.plusMinutes(1), eleven.plusMinutes(1)},
            new LocalTime[] {ten.minusHours(1), twelve},
            new LocalTime[] {ten.plusMinutes(1), eleven.minusMinutes(1)})) {
      assertThrows(
          IllegalArgumentException.class,
          () -> bookings.book(bob, room, day, times[0], times[1], "conflict"));
    }
    assertTrue(bookings.isAvailable(room, day, eleven, twelve));
    assertTrue(bookings.isAvailable(room, day, ten.minusHours(1), ten));
    assertTrue(bookings.isAvailable(room, day.plusDays(1), ten, eleven));
  }

  @Test
  public void invalidInputs() {
    assertThrows(
        IllegalArgumentException.class, () -> bookings.book(null, room, day, ten, eleven, "x"));
    assertThrows(
        IllegalArgumentException.class,
        () -> bookings.book(alice, "missing", day, ten, eleven, "x"));
    assertThrows(
        IllegalArgumentException.class, () -> bookings.book(alice, room, day, ten, ten, "x"));
    assertThrows(
        IllegalArgumentException.class, () -> bookings.book(alice, room, day, eleven, ten, "x"));
    assertThrows(
        IllegalArgumentException.class, () -> bookings.book(alice, room, day, ten, eleven, " "));
    assertThrows(
        IllegalArgumentException.class, () -> bookings.book(alice, room, null, ten, eleven, "x"));
  }

  @Test
  public void modificationIsAtomicAndPreservesIdentity() {
    Booking b = book();
    bookings.book(bob, room, day, eleven, twelve, "occupied");
    assertThrows(
        IllegalArgumentException.class,
        () -> bookings.modifyBooking(alice, b.getBookingId(), room, day, eleven, twelve, "bad"));
    assertEquals(ten, bookings.listBookings(alice).get(0).getStartTime());
    Booking modified =
        bookings.modifyBooking(alice, b.getBookingId(), room, day, ten, eleven, "changed");
    assertEquals(b.getBookingId(), modified.getBookingId());
    assertEquals(b.getUserId(), modified.getUserId());
    assertEquals("changed", modified.getPurpose());
  }

  @Test
  public void ownershipAndDeletion() {
    Booking b = book();
    assertThrows(
        IllegalArgumentException.class, () -> bookings.deleteBooking(bob, b.getBookingId()));
    assertThrows(
        IllegalArgumentException.class, () -> bookings.deleteBooking(admin, b.getBookingId()));
    assertThrows(
        IllegalArgumentException.class,
        () -> bookings.modifyBooking(bob, b.getBookingId(), room, day, ten, eleven, "x"));
    assertThrows(IllegalArgumentException.class, () -> bookings.deleteBooking(alice, "missing"));
    bookings.deleteBooking(alice, b.getBookingId());
    assertTrue(bookings.isAvailable(room, day, ten, eleven));
  }

  @Test
  public void weeklyBoundariesSortingAndPrivacy() {
    bookings.book(alice, room, day.plusDays(7), ten, eleven, "outside");
    bookings.book(alice, room, day.plusDays(6), ten, eleven, "last day");
    book();
    bookings.book(alice, room, day.minusDays(1), ten, eleven, "before");
    List<Booking> view = bookings.getSchedule(bob, room, day);
    assertEquals(2, view.size());
    assertEquals(day, view.get(0).getDate());
    assertEquals("hidden", view.get(0).getUserId());
    assertEquals("U1", bookings.getSchedule(admin, room, day).get(0).getUserId());
    assertEquals("U1", bookings.getSchedule(alice, room, day).get(0).getUserId());
    assertEquals(day.minusDays(1), bookings.listBookings(alice).get(0).getDate());
    assertTrue(bookings.listBookings(bob).isEmpty());
    assertThrows(IllegalArgumentException.class, () -> bookings.getSchedule(null, room, day));
    assertThrows(IllegalArgumentException.class, () -> bookings.getSchedule(alice, "missing", day));
  }

  @Test
  public void searchFiltersAllConditions() throws Exception {
    assertEquals(
        1, search.search(alice, day, ten, eleven, 10, Arrays.asList("WIFI", "projector")).size());
    assertTrue(search.search(alice, day, ten, eleven, 11, Collections.emptyList()).isEmpty());
    assertTrue(search.search(alice, day, ten, eleven, 1, Arrays.asList("wifi", "mic")).isEmpty());
    book();
    assertTrue(search.search(alice, day, ten, eleven, 1, Collections.emptyList()).isEmpty());
    assertEquals(1, search.search(alice, day, eleven, twelve, 1, Collections.emptyList()).size());
    assertThrows(
        IllegalArgumentException.class,
        () -> search.search(null, day, ten, eleven, 1, Collections.emptyList()));
    assertThrows(
        IllegalArgumentException.class,
        () -> search.search(alice, day, ten, eleven, -1, Collections.emptyList()));
  }

  @Test
  public void cascadesAndReturnedListIsolation() throws Exception {
    book();
    bookings.listBookings(alice).clear();
    assertEquals(1, bookings.listBookings(alice).size());
    bookings.cancelBookingsForUser("U1");
    assertTrue(bookings.listBookings(alice).isEmpty());
    book();
    bookings.book(alice, room, LocalDate.of(2020, 1, 1), ten, eleven, "past");
    rooms.deleteRoom(admin, room);
    assertEquals(1, bookings.listBookings(alice).size());
    assertEquals(2020, bookings.listBookings(alice).get(0).getDate().getYear());
  }
}

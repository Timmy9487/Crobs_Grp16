package hk.edu.polyu.comp.comp2021.crobs.model.booking;

import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.model.user.*;
import java.time.*;
import java.util.*;

/** Manages reservations, ownership and availability. */
public class BookingRegistry {
  private final Map<String, Booking> bookings = new HashMap<>();
  private RoomManager rooms;
  private int next = 1;

  public void setRoomManager(RoomManager rooms) {
    this.rooms = Objects.requireNonNull(rooms);
  }

  public static void requireUser(User user) {
    if (user == null) throw new IllegalArgumentException("Please log in first");
  }

  public static void validateSlot(LocalDate day, LocalTime start, LocalTime end) {
    if (day == null || start == null || end == null || !start.isBefore(end))
      throw new IllegalArgumentException("Start time must precede end time on the same day");
  }

  private void validate(
      String room, LocalDate day, LocalTime start, LocalTime end, String purpose, String excluded) {
    validateSlot(day, start, end);
    if (rooms == null || rooms.getRoomById(room) == null)
      throw new IllegalArgumentException("Unknown room: " + room);
    if (purpose == null || purpose.trim().isEmpty())
      throw new IllegalArgumentException("Purpose is required");
    for (Booking b : bookings.values())
      if (!b.getBookingId().equals(excluded) && b.overlaps(room, day, start, end))
        throw new IllegalArgumentException("Room is already booked during this interval");
  }

  public Booking book(
      User user, String room, LocalDate day, LocalTime start, LocalTime end, String purpose) {
    requireUser(user);
    validate(room, day, start, end, purpose, null);
    Booking b =
        new Booking(
            String.format("B%03d", next++), user.getUserId(), room, day, start, end, purpose);
    bookings.put(b.getBookingId(), b);
    return b;
  }

  private Booking owned(User user, String id) {
    requireUser(user);
    Booking b = bookings.get(id);
    if (b == null) throw new IllegalArgumentException("Unknown booking: " + id);
    if (!b.getUserId().equals(user.getUserId()))
      throw new IllegalArgumentException("You can only change your own bookings");
    return b;
  }

  public Booking modifyBooking(
      User user,
      String id,
      String room,
      LocalDate day,
      LocalTime start,
      LocalTime end,
      String purpose) {
    Booking old = owned(user, id);
    validate(room, day, start, end, purpose, id);
    Booking b = new Booking(id, old.getUserId(), room, day, start, end, purpose);
    bookings.put(id, b);
    return b;
  }

  public void deleteBooking(User user, String id) {
    owned(user, id);
    bookings.remove(id);
  }

  public boolean isAvailable(String room, LocalDate day, LocalTime start, LocalTime end) {
    validateSlot(day, start, end);
    for (Booking b : bookings.values()) if (b.overlaps(room, day, start, end)) return false;
    return true;
  }

  private List<Booking> sorted(List<Booking> list) {
    list.sort(
        Comparator.comparing(Booking::getDate)
            .thenComparing(Booking::getStartTime)
            .thenComparing(Booking::getBookingId));
    return list;
  }

  public List<Booking> listBookings(User user) {
    requireUser(user);
    List<Booking> list = new ArrayList<>();
    for (Booking b : bookings.values()) if (b.getUserId().equals(user.getUserId())) list.add(b);
    return sorted(list);
  }

  public List<Booking> getSchedule(User user, String room, LocalDate start) {
    requireUser(user);
    if (rooms == null || rooms.getRoomById(room) == null)
      throw new IllegalArgumentException("Unknown room: " + room);
    Objects.requireNonNull(start, "Week start is required");
    List<Booking> list = new ArrayList<>();
    for (Booking b : bookings.values())
      if (b.getRoomId().equals(room)
          && !b.getDate().isBefore(start)
          && b.getDate().isBefore(start.plusDays(7))) {
        list.add(
            user instanceof AdminUser || b.getUserId().equals(user.getUserId())
                ? b
                : new Booking(
                    b.getBookingId(),
                    "hidden",
                    room,
                    b.getDate(),
                    b.getStartTime(),
                    b.getEndTime(),
                    b.getPurpose()));
      }
    return sorted(list);
  }

  public void cancelBookingsForUser(String id) {
    bookings.values().removeIf(b -> b.getUserId().equals(id));
  }

  public void cancelFutureBookingsForRoom(String id) {
    LocalDateTime now = LocalDateTime.now();
    bookings
        .values()
        .removeIf(b ->
            b.getRoomId().equals(id)
                && LocalDateTime.of(b.getDate(), b.getStartTime()).isAfter(now));
  }
}

package hk.edu.polyu.comp.comp2021.crobs.model;

import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.Booking;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.BookingRegistry;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.user.AdminUser;
import hk.edu.polyu.comp.comp2021.crobs.model.user.RegularUser;
import hk.edu.polyu.comp.comp2021.crobs.util.IDGenerator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class BookingRegistryTest {

    private BookingRegistry bookingRegistry;
    private RoomManager roomManager;

    private AdminUser admin;
    private RegularUser user;

    private Room room;

    @Before
    public void setUp() throws Exception {

        IDGenerator.reset();

        bookingRegistry = new BookingRegistry();

        roomManager = new RoomManager();

        bookingRegistry.setRoomManager(roomManager);

        admin =
                new AdminUser(
                        "U001",
                        "Admin"
                );

        user =
                new RegularUser(
                        "U002",
                        "Alice"
                );

        room =
                roomManager.addRoom(
                        admin,
                        "meetingRoom",
                        "10",
                        "projector"
                );
    }

    @Test
    public void testBookSuccess() {

        Booking booking =
                bookingRegistry.book(
                        user,
                        room.getRoomId(),
                        LocalDate.of(2027, 1, 1),
                        LocalTime.of(10, 0),
                        LocalTime.of(12, 0),
                        "Meeting"
                );

        assertNotNull(booking);

        assertEquals(
                "U002",
                booking.getUserId()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookingConflict() {

        bookingRegistry.book(
                user,
                room.getRoomId(),
                LocalDate.of(2027, 1, 1),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                "Booking A"
        );

        bookingRegistry.book(
                user,
                room.getRoomId(),
                LocalDate.of(2027, 1, 1),
                LocalTime.of(11, 0),
                LocalTime.of(13, 0),
                "Booking B"
        );
    }

    @Test
    public void testModifyBooking() {

        Booking booking =
                bookingRegistry.book(
                        user,
                        room.getRoomId(),
                        LocalDate.of(2027, 1, 1),
                        LocalTime.of(10, 0),
                        LocalTime.of(12, 0),
                        "Meeting"
                );

        Booking modified =
                bookingRegistry.modifyBooking(
                        user,
                        booking.getBookingId(),
                        room.getRoomId(),
                        LocalDate.of(2027, 1, 2),
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0),
                        "Modified"
                );

        assertEquals(
                LocalDate.of(2027, 1, 2),
                modified.getDate()
        );
    }

    @Test
    public void testDeleteBooking() {

        Booking booking =
                bookingRegistry.book(
                        user,
                        room.getRoomId(),
                        LocalDate.of(2027, 1, 1),
                        LocalTime.of(10, 0),
                        LocalTime.of(12, 0),
                        "Meeting"
                );

        bookingRegistry.deleteBooking(
                user,
                booking.getBookingId()
        );

        assertNull(
                bookingRegistry.getBooking(
                        booking.getBookingId()
                )
        );
    }

    @Test
    public void testListBookings() {

        bookingRegistry.book(
                user,
                room.getRoomId(),
                LocalDate.of(2027, 1, 1),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                "A"
        );

        assertEquals(
                1,
                bookingRegistry.listBookings(user)
                        .size()
        );
    }
}
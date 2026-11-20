package hk.edu.polyu.comp.comp2021.crobs.model;

import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.BookingRegistry;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.RoomSearch;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.user.AdminUser;
import hk.edu.polyu.comp.comp2021.crobs.model.user.RegularUser;
import hk.edu.polyu.comp.comp2021.crobs.util.IDGenerator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RoomSearchTest {

    private RoomManager roomManager;

    private BookingRegistry bookingRegistry;

    private RoomSearch roomSearch;

    private RegularUser user;

    private AdminUser admin;

    @Before
    public void setUp() throws Exception {

        IDGenerator.reset();

        roomManager = new RoomManager();

        bookingRegistry = new BookingRegistry();

        bookingRegistry.setRoomManager(roomManager);

        roomSearch =
                new RoomSearch(
                        roomManager,
                        bookingRegistry
                );

        admin =
                new AdminUser(
                        "U001",
                        "Admin"
                );

        user =
                new RegularUser(
                        "U002",
                        "User"
                );

        roomManager.addRoom(
                admin,
                "meetingRoom",
                "10",
                "projector"
        );

        roomManager.addRoom(
                admin,
                "seminarRoom",
                "50",
                "projector,wifi"
        );
    }

    @Test
    public void testSearchByCapacity() {

        List<Room> result =
                roomSearch.search(
                        user,
                        LocalDate.of(2027, 1, 1),
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0),
                        40,
                        Arrays.asList()
                );

        assertEquals(
                1,
                result.size()
        );
    }

    @Test
    public void testSearchByFeature() {

        List<Room> result =
                roomSearch.search(
                        user,
                        LocalDate.of(2027, 1, 1),
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 0),
                        1,
                        Arrays.asList("wifi")
                );

        assertEquals(
                1,
                result.size()
        );
    }
}

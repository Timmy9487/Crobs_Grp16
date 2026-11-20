package hk.edu.polyu.comp.comp2021.crobs.model;

import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.room.RoomType;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class RoomTest {

    @Test
    public void testCreateRoom() throws Exception {

        Room room =
                new Room(
                        "R001",
                        RoomType.MEETING_ROOM,
                        20,
                        Arrays.asList(
                                "projector",
                                "wifi"
                        )
                );

        assertEquals(
                "R001",
                room.getRoomId()
        );

        assertEquals(
                20,
                room.getCapacity()
        );
    }

    @Test
    public void testHasFeature() throws Exception {

        Room room =
                new Room(
                        "R001",
                        RoomType.MEETING_ROOM,
                        20,
                        Arrays.asList(
                                "projector",
                                "wifi"
                        )
                );

        assertTrue(
                room.hasFeatures(
                        Arrays.asList("wifi")
                )
        );

        assertFalse(
                room.hasFeatures(
                        Arrays.asList("whiteboard")
                )
        );
    }

    @Test
    public void testCompareRooms() throws Exception {

        Room r1 =
                new Room(
                        "R001",
                        RoomType.MEETING_ROOM,
                        20,
                        Arrays.asList()
                );

        Room r2 =
                new Room(
                        "R002",
                        RoomType.MEETING_ROOM,
                        20,
                        Arrays.asList()
                );

        assertTrue(
                r1.compareTo(r2) < 0
        );
    }
}
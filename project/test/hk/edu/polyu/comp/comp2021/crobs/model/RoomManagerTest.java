package hk.edu.polyu.comp.comp2021.crobs.model;

import hk.edu.polyu.comp.comp2021.crobs.exception.*;
import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.room.RoomType;
import hk.edu.polyu.comp.comp2021.crobs.model.user.AdminUser;
import hk.edu.polyu.comp.comp2021.crobs.model.user.RegularUser;
import hk.edu.polyu.comp.comp2021.crobs.util.IDGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RoomManagerTest {
    private RoomManager roomManager;
    private AdminUser admin;
    private RegularUser regular;

    @Before
    public void setUp() {
        IDGenerator.reset();
        roomManager = new RoomManager();
        admin = new AdminUser("U001", "AdminBob");
        regular = new RegularUser("U002", "Alice");
    }

    @Test
    public void testAddRoomSuccess() throws Exception {
        Room r = roomManager.addRoom(admin, "lectureTheatre", "150", "projector,mic");
        assertNotNull(r);
        assertEquals("R001", r.getRoomId());
        assertEquals(RoomType.LECTURE_THEATRE, r.getRoomType());
        assertEquals(150, r.getCapacity());
        assertEquals("projector,mic", r.getFeaturesAsString());
    }

    @Test(expected = PermissionDeniedException.class)
    public void testAddRoomPermissionDenied() throws Exception {
        roomManager.addRoom(regular, "meetingRoom", "10", "whiteboard");
    }

    @Test(expected = InvalidCommandException.class)
    public void testAddRoomInvalidType() throws Exception {
        roomManager.addRoom(admin, "invalidRoomType", "10", "whiteboard");
    }

    @Test(expected = InvalidCommandException.class)
    public void testAddRoomInvalidCapacity() throws Exception {
        roomManager.addRoom(admin, "meetingRoom", "-5", "whiteboard");
    }

    @Test
    public void testModifyRoomFields() throws Exception {
        Room r = roomManager.addRoom(admin, "seminarRoom", "40", "wifi");
        roomManager.modifyRoom(admin, r.getRoomId(), "roomType", "meetingRoom");
        assertEquals(RoomType.MEETING_ROOM, r.getRoomType());

        roomManager.modifyRoom(admin, r.getRoomId(), "capacity", "60");
        assertEquals(60, r.getCapacity());

        roomManager.modifyRoom(admin, r.getRoomId(), "features", "tv,sound");
        assertEquals("tv,sound", r.getFeaturesAsString());
    }

    @Test(expected = RoomNotFoundException.class)
    public void testModifyNonExistentRoom() throws Exception {
        roomManager.modifyRoom(admin, "R999", "capacity", "50");
    }

    @Test
    public void testDeleteRoom() throws Exception {
        Room r = roomManager.addRoom(admin, "computerLab", "30", "pc");
        Room deleted = roomManager.deleteRoom(admin, r.getRoomId());
        assertEquals(r.getRoomId(), deleted.getRoomId());
        assertNull(roomManager.getRoomById(r.getRoomId()));
    }

    @Test
    public void testListAllRoomsSorted() throws Exception {
        roomManager.addRoom(admin, "meetingRoom", "10", "whiteboard"); // R001
        roomManager.addRoom(admin, "lectureTheatre", "200", "mic");    // R002
        List<Room> list = roomManager.listAllRooms(regular);
        assertEquals(2, list.size());
        assertEquals("R001", list.get(0).getRoomId());
        assertEquals("R002", list.get(1).getRoomId());
    }
}
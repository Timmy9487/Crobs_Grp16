package hk.edu.polyu.comp.comp2021.crobs.manager;

import hk.edu.polyu.comp.comp2021.crobs.exception.InvalidCommandException;
import hk.edu.polyu.comp.comp2021.crobs.exception.PermissionDeniedException;
import hk.edu.polyu.comp.comp2021.crobs.exception.RoomNotFoundException;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.room.RoomType;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;
import hk.edu.polyu.comp.comp2021.crobs.model.user.AdminUser;
import hk.edu.polyu.comp.comp2021.crobs.util.IDGenerator;

import java.util.*;

public class RoomManager {
    private final Map<String, Room> roomMap = new HashMap<>();
    private BookingManager bookingManager;

    public RoomManager() {
    }

    // links to booking manager to satisfy r9
    public void setBookingManager(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    // verifies if actor has admin
    private void checkAdminPermission(User currentUser) throws PermissionDeniedException {
        if (currentUser == null) {
            throw new PermissionDeniedException("No user is currently logged in. Admin privileges required.");
        }
        // SuperAdmin also extends AdminUser, so checking instanceof AdminUser covers both
        if (!(currentUser instanceof AdminUser)) {
            throw new PermissionDeniedException("Permission denied. Only admin users can perform this operation.");
        }
    }

    // verifies if at least one user is logged in
    private void checkLoggedIn(User currentUser) throws PermissionDeniedException {
        if (currentUser == null) {
            throw new PermissionDeniedException("Please log in first to use this command.");
        }
    }

    // ==========================================
    // REQ7: addRoom roomType capacity features
    // ==========================================
    public Room addRoom(User currentUser, String roomTypeStr, String capacityStr, String featuresStr)
            throws PermissionDeniedException, InvalidCommandException {
        checkAdminPermission(currentUser);

        RoomType roomType = RoomType.fromString(roomTypeStr);

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr.trim());
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Capacity must be a valid integer: " + capacityStr);
        }

        if (capacity <= 0) {
            throw new InvalidCommandException("Capacity must be strictly greater than 0.");
        }

        List<String> featureList = parseFeatures(featuresStr);
        String newRoomId = IDGenerator.generateRoomId();
        while (roomMap.containsKey(newRoomId)) {
            newRoomId = IDGenerator.generateRoomId();
        }

        Room newRoom = new Room(newRoomId, roomType, capacity, featureList);
        roomMap.put(newRoomId, newRoom);
        return newRoom;
    }

    // ==========================================
    // REQ8: modifyRoom roomID field newValue
    // ==========================================
    public Room modifyRoom(User currentUser, String roomId, String field, String newValue)
            throws PermissionDeniedException, RoomNotFoundException, InvalidCommandException {
        checkAdminPermission(currentUser);

        if (roomId == null || roomId.trim().isEmpty()) {
            throw new InvalidCommandException("Room ID cannot be empty.");
        }
        Room room = roomMap.get(roomId.trim());
        if (room == null) {
            throw new RoomNotFoundException("Room with ID " + roomId + " does not exist.");
        }
        if (field == null || field.trim().isEmpty()) {
            throw new InvalidCommandException("Field name cannot be empty.");
        }
        if (newValue == null) {
            throw new InvalidCommandException("New value cannot be null.");
        }

        String normalizedField = field.trim().toLowerCase();
        switch (normalizedField) {
            case "roomtype":
            case "type":
                RoomType newType = RoomType.fromString(newValue);
                room.setRoomType(newType);
                break;

            case "capacity":
                try {
                    int newCap = Integer.parseInt(newValue.trim());
                    room.setCapacity(newCap);
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("Capacity must be a valid integer: " + newValue);
                }
                break;

            case "features":
                room.setFeatures(parseFeatures(newValue));
                break;

            default:
                throw new InvalidCommandException("Unsupported field '" + field 
                    + "'. Supported fields to modify: roomType, capacity, features.");
        }

        return room;
    }

    // ==========================================
    // REQ9: deleteRoom roomID
    // ==========================================
    public Room deleteRoom(User currentUser, String roomId)
            throws PermissionDeniedException, RoomNotFoundException, InvalidCommandException {
        checkAdminPermission(currentUser);

        if (roomId == null || roomId.trim().isEmpty()) {
            throw new InvalidCommandException("Room ID cannot be empty.");
        }
        String cleanId = roomId.trim();
        Room room = roomMap.remove(cleanId);
        if (room == null) {
            throw new RoomNotFoundException("Room with ID " + roomId + " does not exist.");
        }

        // cancel all future bookings for that room
        if (bookingManager != null) {
            bookingManager.cancelFutureBookingsForRoom(cleanId);
        }

        return room;
    }

    // ==========================================
    // REQ15: listRoom roomID
    // ==========================================
    public Room listRoom(User currentUser, String roomId)
            throws PermissionDeniedException, RoomNotFoundException, InvalidCommandException {
        checkLoggedIn(currentUser);

        if (roomId == null || roomId.trim().isEmpty()) {
            throw new InvalidCommandException("Room ID cannot be empty.");
        }
        Room room = roomMap.get(roomId.trim());
        if (room == null) {
            throw new RoomNotFoundException("Room with ID " + roomId + " does not exist.");
        }
        return room;
    }

    // ==========================================
    // REQ16: listAllRooms
    // ==========================================
    public List<Room> listAllRooms(User currentUser) throws PermissionDeniedException {
        checkLoggedIn(currentUser);

        List<Room> rooms = new ArrayList<>(roomMap.values());
        Collections.sort(rooms);
        return rooms;
    }

    // parser for comma-separated feature strings without spaces.
    private List<String> parseFeatures(String featuresStr) {
        List<String> list = new ArrayList<>();
        if (featuresStr != null && !featuresStr.trim().isEmpty()) {
            String[] parts = featuresStr.split(",");
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    list.add(part.trim());
                }
            }
        }
        return list;
    }

    public Room getRoomById(String roomId) {
        return roomMap.get(roomId);
    }

    public Map<String, Room> getAllRoomsMap() {
        return roomMap;
    }
}
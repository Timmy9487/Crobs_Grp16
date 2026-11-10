package hk.edu.polyu.comp.comp2021.crobs.model.room;

import hk.edu.polyu.comp.comp2021.crobs.exception.InvalidCommandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Room implements Comparable<Room> {
    private final String roomId;
    private RoomType roomType;
    private int capacity;
    private List<String> features;

    /**
     * constructs room instance
     *
     * @param roomId   unique identifier of the room (e.g., R001)
     * @param roomType type of the room
     * @param capacity seating/student capacity (> 0)
     * @param features list of features
     */
    public Room(String roomId, RoomType roomType, int capacity, List<String> features) throws InvalidCommandException {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new InvalidCommandException("Room ID cannot be null or empty.");
        }
        this.roomId = roomId.trim();
        setRoomType(roomType);
        setCapacity(capacity);
        setFeatures(features);
    }

    public String getRoomId() {
        return roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) throws InvalidCommandException {
        if (roomType == null) {
            throw new InvalidCommandException("Room type cannot be null.");
        }
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) throws InvalidCommandException {
        if (capacity <= 0) {
            throw new InvalidCommandException("Room capacity must be a positive integer.");
        }
        this.capacity = capacity;
    }

    public List<String> getFeatures() {
        return Collections.unmodifiableList(features);
    }

    public void setFeatures(List<String> features) {
        if (features == null) {
            this.features = new ArrayList<>();
        } else {
            List<String> cleaned = new ArrayList<>();
            for (String f : features) {
                if (f != null && !f.trim().isEmpty()) {
                    cleaned.add(f.trim());
                }
            }
            this.features = cleaned;
        }
    }

    /**
     * returns features formatted as a comma-separated string without spaces
     */
    public String getFeaturesAsString() {
        return String.join(",", features);
    }

    /**
     * checks if this room contains all required features (case-insensitive)
     *
     * @param requiredFeatures list of requested features
     * @return true if all required features are present
     */
    public boolean hasFeatures(List<String> requiredFeatures) {
        if (requiredFeatures == null || requiredFeatures.isEmpty()) {
            return true;
        }
        for (String req : requiredFeatures) {
            boolean matched = false;
            for (String current : features) {
                if (current.equalsIgnoreCase(req.trim())) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(Room other) {
        return this.roomId.compareTo(other.roomId);
    }

    @Override
    public String toString() {
        return String.format("Room ID: %s | Type: %s | Capacity: %d | Features: %s",
                roomId, roomType.getTypeName(), capacity, getFeaturesAsString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return Objects.equals(roomId, room.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }
}
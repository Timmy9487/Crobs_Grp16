package hk.edu.polyu.comp.comp2021.crobs.model.room;

import hk.edu.polyu.comp.comp2021.crobs.exception.InvalidCommandException;

// enum for supported room types
public enum RoomType {
    LECTURE_THEATRE("lectureTheatre"),
    COMPUTER_LAB("computerLab"),
    SEMINAR_ROOM("seminarRoom"),
    MEETING_ROOM("meetingRoom");

    private final String typeName;

    RoomType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    /**
     * parses a string into a matching RoomType
     *
     * @param text string representation (case-sensitive as per spec or case-insensitive fallback)
     * @return corresponding RoomType
     * @throws InvalidCommandException if room type is unknown
     */
    public static RoomType fromString(String text) throws InvalidCommandException {
        if (text == null || text.trim().isEmpty()) {
            throw new InvalidCommandException("Room type cannot be empty.");
        }
        for (RoomType rt : RoomType.values()) {
            if (rt.typeName.equalsIgnoreCase(text.trim())) {
                return rt;
            }
        }
        throw new InvalidCommandException("Invalid room type: " + text 
            + ". Supported types: lectureTheatre, computerLab, seminarRoom, meetingRoom.");
    }

    @Override
    public String toString() {
        return typeName;
    }
}
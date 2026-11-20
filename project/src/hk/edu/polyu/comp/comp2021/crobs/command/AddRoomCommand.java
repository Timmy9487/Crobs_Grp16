package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

public class AddRoomCommand implements Command {

    private final RoomManager roomManager;

    private final User currentUser;

    private final String roomType;

    private final String capacity;

    private final String features;

    private Room createdRoom;

    public AddRoomCommand(
            RoomManager roomManager,
            User currentUser,
            String roomType,
            String capacity,
            String features) {

        this.roomManager = roomManager;
        this.currentUser = currentUser;
        this.roomType = roomType;
        this.capacity = capacity;
        this.features = features;
    }

    @Override
    public void execute() {

        try {

            createdRoom =
                    roomManager.addRoom(
                            currentUser,
                            roomType,
                            capacity,
                            features
                    );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to add room.",
                    e
            );
        }
    }

    @Override
    public void undo() {

        if (createdRoom == null) {
            return;
        }

        try {

            roomManager.deleteRoom(
                    currentUser,
                    createdRoom.getRoomId()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to undo addRoom.",
                    e
            );
        }
    }
}

package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

public class ModifyRoomCommand implements Command {

    private final RoomManager roomManager;

    private final User currentUser;

    private final String roomId;

    private final String field;

    private final String newValue;

    // backup data for undo
    private String oldValue;

    public ModifyRoomCommand(
            RoomManager roomManager,
            User currentUser,
            String roomId,
            String field,
            String newValue) {

        this.roomManager = roomManager;
        this.currentUser = currentUser;
        this.roomId = roomId;
        this.field = field;
        this.newValue = newValue;
    }

    @Override
    public void execute() {

        try {

            Room room =
                    roomManager.getRoomById(
                            roomId
                    );

            if (room == null) {
                throw new RuntimeException(
                        "Room not found."
                );
            }

            switch (field.toLowerCase()) {

                case "roomtype":
                case "type":

                    oldValue =
                            room.getRoomType()
                                    .getTypeName();
                    break;

                case "capacity":

                    oldValue =
                            String.valueOf(
                                    room.getCapacity()
                            );
                    break;

                case "features":

                    oldValue =
                            room.getFeaturesAsString();
                    break;

                default:
                    throw new RuntimeException(
                            "Unsupported field: "
                                    + field
                    );
            }

            roomManager.modifyRoom(
                    currentUser,
                    roomId,
                    field,
                    newValue
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to modify room.",
                    e
            );
        }
    }

    @Override
    public void undo() {

        try {

            roomManager.modifyRoom(
                    currentUser,
                    roomId,
                    field,
                    oldValue
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to undo modifyRoom.",
                    e
            );
        }
    }
}

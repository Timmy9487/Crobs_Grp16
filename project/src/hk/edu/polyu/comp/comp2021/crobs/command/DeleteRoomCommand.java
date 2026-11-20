package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.manager.BookingManager;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.Booking;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class DeleteRoomCommand implements Command {

    private final RoomManager roomManager;

    private final BookingManager bookingManager;

    private final User currentUser;

    private final String roomId;

    private Room deletedRoom;

    private List<Booking> cancelledBookings;

    public DeleteRoomCommand(
            RoomManager roomManager,
            BookingManager bookingManager,
            User currentUser,
            String roomId) {

        this.roomManager = roomManager;
        this.bookingManager = bookingManager;
        this.currentUser = currentUser;
        this.roomId = roomId;

        this.cancelledBookings =
                new ArrayList<>();
    }

    @Override
    public void execute() {

        try {

            deletedRoom =
                    roomManager.getRoom(
                            roomId
                    );

            if (deletedRoom == null) {

                throw new RuntimeException(
                        "Room not found: "
                                + roomId
                );
            }

            cancelledBookings =
                    bookingManager
                            .getFutureBookingsForRoom(
                                    roomId
                            );

            roomManager.deleteRoom(
                    currentUser,
                    roomId
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to delete room.",
                    e
            );
        }
    }

    @Override
    public void undo() {

        try {

            if (deletedRoom == null) {
                return;
            }

            roomManager.restoreRoom(
                    deletedRoom
            );

            for (Booking booking :
                    cancelledBookings) {

                bookingManager.restoreBooking(
                        booking
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to undo room deletion.",
                    e
            );
        }
    }
}

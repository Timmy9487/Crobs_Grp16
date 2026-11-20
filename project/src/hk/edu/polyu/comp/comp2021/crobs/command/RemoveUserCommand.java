package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.manager.BookingManager;
import hk.edu.polyu.comp.comp2021.crobs.manager.UserManager;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.Booking;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class RemoveUserCommand implements Command {

    private final UserManager userManager;

    private final BookingManager bookingManager;

    private final String userId;

    private User deletedUser;

    private List<Booking> deletedBookings;

    public RemoveUserCommand(
            UserManager userManager,
            BookingManager bookingManager,
            String userId) {

        this.userManager = userManager;
        this.bookingManager = bookingManager;
        this.userId = userId;

        this.deletedBookings = new ArrayList<>();
    }

    @Override
    public void execute() {

        // Save user for undo
        deletedUser =
                userManager.getUser(userId);

        // Save bookings before removal
        deletedBookings =
                bookingManager.getBookingsForUser(
                        userId
                );

        try {

            userManager.removeUser(userId);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to remove user.",
                    e
            );
        }
    }

    @Override
    public void undo() {

        if (deletedUser == null) {
            return;
        }

        // Restore user
        userManager.restoreUser(
                deletedUser
        );

        // Restore cancelled bookings
        for (Booking booking :
                deletedBookings) {

            bookingManager.restoreBooking(
                    booking
            );
        }
    }
}

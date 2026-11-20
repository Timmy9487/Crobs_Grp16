package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.manager.BookingManager;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.Booking;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

public class DeleteBookingCommand implements Command {

    private final BookingManager bookingManager;

    private final User user;

    private final String bookingId;

    private Booking deletedBooking;

    public DeleteBookingCommand(
            BookingManager bookingManager,
            User user,
            String bookingId) {

        this.bookingManager = bookingManager;
        this.user = user;
        this.bookingId = bookingId;
    }

    @Override
    public void execute() {

        deletedBooking =
                bookingManager.getBooking(
                        bookingId
                );

        bookingManager.deleteBooking(
                user,
                bookingId
        );
    }

    @Override
    public void undo() {

        if (deletedBooking == null) {
            return;
        }

        bookingManager.restoreBooking(
                deletedBooking
        );
    }
}
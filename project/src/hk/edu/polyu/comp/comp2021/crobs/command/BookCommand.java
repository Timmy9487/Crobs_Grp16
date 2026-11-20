package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.manager.BookingManager;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.Booking;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookCommand implements Command {

    private final BookingManager bookingManager;

    private final User user;

    private final String roomId;

    private final LocalDate date;

    private final LocalTime startTime;

    private final LocalTime endTime;

    private final String purpose;

    private Booking createdBooking;

    public BookCommand(
            BookingManager bookingManager,
            User user,
            String roomId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            String purpose) {

        this.bookingManager = bookingManager;
        this.user = user;
        this.roomId = roomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
    }

    @Override
    public void execute() {

        createdBooking =
                bookingManager.book(
                        user,
                        roomId,
                        date,
                        startTime,
                        endTime,
                        purpose
                );
    }

    @Override
    public void undo() {

        if (createdBooking == null) {
            return;
        }

        bookingManager.deleteBookingDirect(
                createdBooking.getBookingId()
        );
    }
}

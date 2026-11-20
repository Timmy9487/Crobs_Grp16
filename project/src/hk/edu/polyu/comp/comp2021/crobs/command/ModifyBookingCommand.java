package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.manager.BookingManager;
import hk.edu.polyu.comp.comp2021.crobs.model.booking.Booking;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class ModifyBookingCommand implements Command {

    private final BookingManager bookingManager;

    private final User user;

    private final String bookingId;

    private final String roomId;

    private final LocalDate date;

    private final LocalTime startTime;

    private final LocalTime endTime;

    private final String purpose;

    private Booking originalBooking;

    public ModifyBookingCommand(
            BookingManager bookingManager,
            User user,
            String bookingId,
            String roomId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            String purpose) {

        this.bookingManager = bookingManager;
        this.user = user;

        this.bookingId = bookingId;
        this.roomId = roomId;

        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

        this.purpose = purpose;
    }

    @Override
    public void execute() {

        originalBooking =
                bookingManager.getBooking(
                        bookingId
                );

        bookingManager.modifyBooking(
                user,
                bookingId,
                roomId,
                date,
                startTime,
                endTime,
                purpose
        );
    }

    @Override
    public void undo() {

        if (originalBooking == null) {
            return;
        }

        bookingManager.restoreBooking(
                originalBooking
        );
    }
}

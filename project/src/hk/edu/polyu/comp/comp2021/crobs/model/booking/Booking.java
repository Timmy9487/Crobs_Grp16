package hk.edu.polyu.comp.comp2021.crobs.model.booking;

import java.time.*;

/** Immutable reservation: updates replace the record, preserving ID and owner. */
public final class Booking {
  private final String bookingId, userId, roomId, purpose;
  private final LocalDate date;
  private final LocalTime startTime, endTime;

  public Booking(
      String id,
      String owner,
      String room,
      LocalDate day,
      LocalTime start,
      LocalTime end,
      String purpose) {
    if (id == null
        || owner == null
        || room == null
        || day == null
        || start == null
        || end == null
        || purpose == null
        || purpose.trim().isEmpty()
        || !start.isBefore(end)) throw new IllegalArgumentException("Invalid booking data");
    bookingId = id;
    userId = owner;
    roomId = room;
    date = day;
    startTime = start;
    endTime = end;
    this.purpose = purpose;
  }

  public String getBookingId() {
    return bookingId;
  }

  public String getUserId() {
    return userId;
  }

  public String getRoomId() {
    return roomId;
  }

  public String getPurpose() {
    return purpose;
  }

  public LocalDate getDate() {
    return date;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public boolean overlaps(String room, LocalDate day, LocalTime start, LocalTime end) {
    return roomId.equals(room)
        && date.equals(day)
        && startTime.isBefore(end)
        && start.isBefore(endTime);
  }
}

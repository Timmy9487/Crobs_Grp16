package hk.edu.polyu.comp.comp2021.crobs.model.booking;

import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;
import java.time.*;
import java.util.*;

public class RoomSearch {
  private final RoomManager rooms;
  private final BookingRegistry bookings;

  public RoomSearch(RoomManager rooms, BookingRegistry bookings) {
    this.rooms = rooms;
    this.bookings = bookings;
  }

  public List<Room> search(
      User user,
      LocalDate day,
      LocalTime start,
      LocalTime end,
      int capacity,
      List<String> features) {
    BookingRegistry.requireUser(user);
    BookingRegistry.validateSlot(day, start, end);
    if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
    List<Room> result = new ArrayList<>();
    for (Room r : rooms.getAllRoomsMap().values())
      if (r.getCapacity() >= capacity
          && r.hasFeatures(features)
          && bookings.isAvailable(r.getRoomId(), day, start, end)) result.add(r);
    Collections.sort(result);
    return result;
  }
}

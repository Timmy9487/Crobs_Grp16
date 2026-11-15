package hk.edu.polyu.comp.comp2021.crobs.service;

import hk.edu.polyu.comp.comp2021.crobs.manager.*;

public class SearchService extends hk.edu.polyu.comp.comp2021.crobs.model.booking.RoomSearch {
  public SearchService(RoomManager rooms, BookingManager bookings) {
    super(rooms, bookings);
  }
}

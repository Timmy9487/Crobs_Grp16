package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.exception.InvalidCommandException;
import hk.edu.polyu.comp.comp2021.crobs.exception.PermissionDeniedException;
import hk.edu.polyu.comp.comp2021.crobs.exception.RoomNotFoundException;
import hk.edu.polyu.comp.comp2021.crobs.exception.UserNotFoundException;
import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.manager.UserManager;
import hk.edu.polyu.comp.comp2021.crobs.model.CROBS;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

import java.util.List;
import java.util.Arrays;
/**
 * CommandProcessor
 *
 * Responsibilities:
 * - Receives raw command strings from CROBS
 * - Routes commands to the corresponding module
 * - Handles command-level validation
 * - Displays user-friendly error messages
 *
 * REQ18:
 * - Handles "quit" command by calling CROBS.quit()
 */
public class CommandProcessor {

    private final CROBS crobs;

    public CommandProcessor(CROBS crobs) {
        this.crobs = crobs;
    }

    // determines which model handles the command
    public void process(String commandLine) {
        if (commandLine == null || commandLine.trim().isEmpty()) {
            return;
        }

        String[] tokens = commandLine.trim().split("\\s+");
        String cmd = tokens[0];
        // ============================================================
        // REQ18: quit
        // Terminates the current CROBS session.
        // The actual termination is handled by CROBS.quit().
        // ============================================================
        if (cmd.equalsIgnoreCase("quit")) {
            crobs.quit();
            return;
        }
        
        try {
            if (handleRoomCommand(cmd, tokens)) return;
            if (handleUserCommand(cmd, tokens)) return;
            if (handleBookingCommand(cmd, tokens)) return;

            System.out.println("Error: Unknown command '" + cmd + "'");

        } catch (PermissionDeniedException | RoomNotFoundException | InvalidCommandException | UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException | java.time.DateTimeException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: Unexpected error occurred: " + e.getMessage());
        }
    }

    // =========================================================================
    // USER MODULE (REQ 2, 3, 4, 5, 6)
    // =========================================================================
    private boolean handleUserCommand(String cmd, String[] args)
            throws PermissionDeniedException, UserNotFoundException {

        UserManager um = crobs.getUserManager();

        switch (cmd) {
            case "addUser":
                if (args.length < 2) {
                    System.out.println("Error: Usage: addUser name");
                } else {
                    String fullName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    um.addUser(fullName);
                }
                return true;

            case "removeUser":
                if (args.length < 2) {
                    System.out.println("Error: Usage: removeUser targetUserID");
                } else {
                    um.removeUser(args[1]);
                    crobs.setCurrentUser(um.getCurrentUser()); // Defensive sync
                }
                return true;

            case "changeRole":
                if (args.length < 3) {
                    System.out.println("Error: Usage: changeRole targetUserID newRole");
                } else {
                    um.changeRole(args[1], args[2]);
                    crobs.setCurrentUser(um.getCurrentUser()); // Sync for self-demotion
                }
                return true;

            case "login":
                if (args.length < 2) {
                    System.out.println("Error: Usage: login userID");
                } else if (um.getCurrentUser() != null) {
                    System.out.println("Error: Already logged in as "
                            + um.getCurrentUser().getName() + ". Log out first.");
                } else {
                    um.login(args[1]);
                    crobs.setCurrentUser(um.getCurrentUser()); // Sync to CROBS
                }
                return true;

            case "logout":
                um.logout();
                crobs.setCurrentUser(null); // Sync to CROBS
                return true;

            default:
                return false;
        }
    }

    // =========================================================================
    // ROOM MODULE (REQ 7, 8, 9, 15, 16)
    // =========================================================================
    private boolean handleRoomCommand(String cmd, String[] args) throws Exception {
        RoomManager rm = crobs.getRoomManager();
        User user = crobs.getCurrentUser();

        switch (cmd) {
            case "addRoom": // REQ7: addRoom roomType capacity features
                if (args.length < 4) {
                    System.out.println("Error: Usage: addRoom roomType capacity features");
                } else {
                    Room r = rm.addRoom(user, args[1], args[2], args[3]);
                    System.out.println("Room added successfully: " + r.getRoomId());
                }
                return true;

            case "modifyRoom": // REQ8: modifyRoom roomID field newValue
                if (args.length < 4) {
                    System.out.println("Error: Usage: modifyRoom roomID field newValue");
                } else {
                    Room r = rm.modifyRoom(user, args[1], args[2], args[3]);
                    System.out.println("Room " + r.getRoomId() + " modified successfully.");
                }
                return true;

            case "deleteRoom": // REQ9: deleteRoom roomID
                if (args.length < 2) {
                    System.out.println("Error: Usage: deleteRoom roomID");
                } else {
                    Room r = rm.deleteRoom(user, args[1]);
                    System.out.println("Room " + r.getRoomId() + " deleted successfully.");
                }
                return true;

            case "listRoom": // REQ15: listRoom roomID
                if (args.length < 2) {
                    System.out.println("Error: Usage: listRoom roomID");
                } else {
                    Room r = rm.listRoom(user, args[1]);
                    printRoom(r);
                }
                return true;

            case "listAllRooms": // REQ16: listAllRooms
                List<Room> rooms = rm.listAllRooms(user);
                if (rooms.isEmpty()) {
                    System.out.println("No rooms available.");
                } else {
                    for (Room r : rooms) {
                        printRoom(r);
                    }
                }
                return true;

            default:
                return false; // not a room command, passes to next module
        }
    }

    private void printRoom(Room r) {
        System.out.println(r.getRoomId() + " " + r.getRoomType().getTypeName() + " " 
                           + r.getCapacity() + " " + r.getFeaturesAsString());
    }

    // =========================================================================
    // BOOKING & SEARCH MODULE (REQ 10, 11, 12, 13, 14, 17)
    // =========================================================================
  private boolean handleBookingCommand(String cmd, String[] args) throws Exception {
    User user = crobs.getCurrentUser();
    hk.edu.polyu.comp.comp2021.crobs.manager.BookingManager bm = crobs.getBookingManager();
    switch (cmd) {
      case "book":
        minimum(args, 6);
        printBooking(
            bm.book(user, args[1], date(args[2]), time(args[3]), time(args[4]), purpose(args, 5)));
        return true;
      case "modifyBooking":
        minimum(args, 7);
        printBooking(
            bm.modifyBooking(
                user,
                args[1],
                args[2],
                date(args[3]),
                time(args[4]),
                time(args[5]),
                purpose(args, 6)));
        return true;
      case "deleteBooking":
        count(args, 2);
        bm.deleteBooking(user, args[1]);
        System.out.println("Booking cancelled: " + args[1]);
        return true;
      case "search":
        count(args, 6);
        List<Room> found =
            crobs
                .getSearchService()
                .search(
                    user,
                    date(args[1]),
                    time(args[2]),
                    time(args[3]),
                    Integer.parseInt(args[4]),
                    args[5].equals("-")
                        ? java.util.Collections.emptyList()
                        : java.util.Arrays.asList(args[5].split(",")));
        if (found.isEmpty()) System.out.println("No available rooms.");
        for (Room r : found) printRoom(r);
        return true;
      case "schedule":
        count(args, 3);
        printBookings(bm.getSchedule(user, args[1], date(args[2])));
        return true;
      case "listBookings":
        count(args, 1);
        printBookings(bm.listBookings(user));
        return true;
      default:
        return false;
    }
  }

  private static void count(String[] args, int n) {
    if (args.length != n)
      throw new IllegalArgumentException("Incorrect number of arguments for " + args[0]);
  }

  private static void minimum(String[] args, int n) {
    if (args.length < n) throw new IllegalArgumentException("Missing arguments for " + args[0]);
  }

  private static java.time.LocalDate date(String s) {
    return java.time.LocalDate.parse(s);
  }

  private static java.time.LocalTime time(String s) {
    return java.time.LocalTime.parse(s);
  }

  private static String purpose(String[] args, int offset) {
    return String.join(" ", java.util.Arrays.copyOfRange(args, offset, args.length));
  }

  private static void printBooking(hk.edu.polyu.comp.comp2021.crobs.model.booking.Booking b) {
    System.out.println(
        b.getBookingId()
            + " "
            + b.getRoomId()
            + " "
            + b.getDate()
            + " "
            + b.getStartTime()
            + " "
            + b.getEndTime()
            + " owner="
            + b.getUserId()
            + " "
            + b.getPurpose());
  }

  private static void printBookings(
      List<hk.edu.polyu.comp.comp2021.crobs.model.booking.Booking> list) {
    if (list.isEmpty()) System.out.println("No bookings.");
    for (hk.edu.polyu.comp.comp2021.crobs.model.booking.Booking b : list) printBooking(b);
  }
}

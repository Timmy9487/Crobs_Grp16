package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.exception.InvalidCommandException;
import hk.edu.polyu.comp.comp2021.crobs.exception.PermissionDeniedException;
import hk.edu.polyu.comp.comp2021.crobs.exception.RoomNotFoundException;
import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.model.CROBS;
import hk.edu.polyu.comp.comp2021.crobs.model.room.Room;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

import java.util.List;
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

        } catch (PermissionDeniedException | RoomNotFoundException | InvalidCommandException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: Unexpected error occurred: " + e.getMessage());
        }
    }

    // =========================================================================
    // USER MODULE (REQ 2, 3, 4, 5, 6)
    // =========================================================================
    private boolean handleUserCommand(String cmd, String[] args) throws Exception {
        switch (cmd) {
            case "addUser":
            case "removeUser":
            case "changeRole":
            case "login":
            case "logout":
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
        switch (cmd) {
            case "book":
            case "modifyBooking":
            case "deleteBooking":
            case "search":
            case "schedule":
            case "listBookings":
                return true;
            default:
                return false;
        }
    }
}

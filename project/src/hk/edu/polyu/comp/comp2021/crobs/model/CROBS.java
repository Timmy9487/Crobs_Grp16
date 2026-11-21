package hk.edu.polyu.comp.comp2021.crobs.model;

import hk.edu.polyu.comp.comp2021.crobs.command.CommandProcessor;
import hk.edu.polyu.comp.comp2021.crobs.logging.LogManager;
import hk.edu.polyu.comp.comp2021.crobs.manager.BookingManager;
import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.manager.UserManager;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;
import hk.edu.polyu.comp.comp2021.crobs.service.SearchService;

import java.util.Scanner;

/**
 * Main CROBS System.
 *
 * Responsibilities:
 * - Initialize managers and services
 * - Maintain application lifecycle
 * - Run CLI loop
 * - Coordinate REQ1 logging
 * - Coordinate REQ18 termination
 */
public class CROBS {

    /* ===============================
       Core Managers
       =============================== */

    private final UserManager userManager;

    private final RoomManager roomManager;

    private final BookingManager bookingManager;

    private final SearchService searchService;

    /* ===============================
       Command Processing
       =============================== */

    private final CommandProcessor commandProcessor;

    /* ===============================
       Logging
       =============================== */

    private final LogManager logManager;

    /* ===============================
       Current Session
       =============================== */

    private User currentUser;

    /* ===============================
       Program State
       =============================== */

    private boolean running;

    /**
     * System constructor.
     */
    public CROBS(LogManager logManager) {

        this.logManager = logManager;

        roomManager = new RoomManager();

        bookingManager = new BookingManager();

        userManager = new UserManager(
                bookingManager
        );

        bookingManager.setRoomManager(
                roomManager
        );

        roomManager.setBookingManager(
                bookingManager
        );

        searchService =
                new SearchService(
                        roomManager,
                        bookingManager
                );

        commandProcessor =
                new CommandProcessor(this);

        currentUser = null;

        running = true;
    }

    /**
     * Starts CROBS CLI.
     */
    public void start() {

        Scanner scanner =
                new Scanner(System.in);

        System.out.println(
                "\n================================="
        );

        System.out.println(
                "Welcome to CROBS"
        );

        System.out.println(
                "Classroom Room Booking System"
        );

        System.out.println(
                "Default SuperAdmin ID: U000"
        );

        System.out.println(
                "Type 'help' to view commands."
        );

        System.out.println(
                "=================================\n"
        );

        while (running) {

            System.out.print("CROBS> ");

            String command =
                    scanner.nextLine().trim();

            if (command.isEmpty()) {
                continue;
            }

            try {

                /* REQ1 */
                logManager.log(command);

                commandProcessor.process(command);

            } catch (Exception e) {

                System.out.println(
                        "System Error: "
                                + e.getMessage()
                );
            }
        }

        logManager.close();

        scanner.close();
    }

    /**
     * REQ18
     */
    public void quit() {

        System.out.println(
                "CROBS terminated."
        );

        running = false;
    }

    /* ===============================
       Getters
       =============================== */

    public UserManager getUserManager() {
        return userManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public BookingManager getBookingManager() {
        return bookingManager;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(
            User currentUser) {

        this.currentUser =
                currentUser;
    }

}

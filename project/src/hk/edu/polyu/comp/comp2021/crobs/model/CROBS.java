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
 * Main CROBS system class.
 *
 * Responsibilities:
 * - Maintain system state
 * - Hold references to managers/services
 * - Maintain current logged-in user
 * - Run the command loop
 * - Handle REQ18 (quit)
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
       Command Handling
       =============================== */

    private final CommandProcessor commandProcessor;

    /* ===============================
       Logging (REQ1)
       =============================== */

    private final LogManager logManager;

    /* ===============================
       Current Session
       =============================== */

    private User currentUser;

    /* ===============================
       REQ18 Program State
       =============================== */

    private boolean running;

    /**
     * Constructor.
     *
     * @param logManager handles HTML and TXT logging
     */
    public CROBS(LogManager logManager) {

        // Store logger
        this.logManager = logManager;

        // Create managers
        userManager = new UserManager();
        roomManager = new RoomManager();
        bookingManager = new BookingManager();

        /*
         * Allows RoomManager to access bookings.
         * Needed later for REQ9:
         * deleteRoom() must cancel future bookings.
         */
        roomManager.setBookingManager(bookingManager);

        // Create services
        searchService =
                new SearchService(
                        roomManager,
                        bookingManager
                );

        // Command dispatcher
        commandProcessor =
                new CommandProcessor(this);

        // Session starts with nobody logged in
        currentUser = null;

        // Program starts running
        running = true;

        // Create default accounts
        initializeDefaultUsers();
    }

    /**
     * Creates default users.
     *
     * Example:
     * - SUPERADMIN
     * - initial ADMIN
     */
    private void initializeDefaultUsers() {

        // Example implementation later:
        //
        // userManager.createSuperAdmin(
        //      "U000",
        //      "superadmin");
    }

    /**
     * Starts the CLI loop.
     *
     * REQ18:
     * quit -> terminate system
     */
    public void start() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to CROBS");

        while (running) {

            System.out.print("> ");

            String command =
                    scanner.nextLine().trim();

            // Ignore empty input
            if (command.isEmpty()) {
                continue;
            }

            // REQ1:
            // Log every command entered
            logManager.log(command);

            // REQ18:
            // quit command
            if (command.equalsIgnoreCase("quit")) {

                quit();

                continue;
            }

            // Process all other commands
            commandProcessor.process(command);
        }

        // Close log files before program exits
        logManager.close();

        scanner.close();
    }

    /**
     * REQ18
     * Terminates CROBS.
     */
    public void quit() {

        System.out.println("CROBS terminated.");

        running = false;
    }

    /* ===============================
       Getters / Setters
       Used by CommandProcessor
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

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public LogManager getLogManager() {
        return logManager;
    }
}

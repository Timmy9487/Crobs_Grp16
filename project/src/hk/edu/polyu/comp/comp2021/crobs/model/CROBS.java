package hk.edu.polyu.comp.comp2021.crobs.model;

import hk.edu.polyu.comp.comp2021.crobs.command.CommandProcessor;
import hk.edu.polyu.comp.comp2021.crobs.manager.BookingManager;
import hk.edu.polyu.comp.comp2021.crobs.manager.RoomManager;
import hk.edu.polyu.comp.comp2021.crobs.manager.UserManager;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;
import hk.edu.polyu.comp.comp2021.crobs.service.SearchService;

import java.util.Scanner;

public class CROBS {

    private UserManager userManager;
    private RoomManager roomManager;
    private BookingManager bookingManager;
    private SearchService searchService;
    private CommandProcessor commandProcessor;
    private User currentUser; // starts at null (r5)

    public CROBS(String htmlLogFile, String txtLogFile) {
        userManager = new UserManager();
        roomManager = new RoomManager();
        bookingManager = new BookingManager();

        // wire RoomManager to BookingManager so REQ9 can cancel future bookings when deleting a room
        roomManager.setBookingManager(bookingManager);

        searchService = new SearchService(roomManager, bookingManager);
        commandProcessor = new CommandProcessor(this);

        initializeDefaultUsers();
    }

    private void initializeDefaultUsers() {
        // creates default superadmin user at startup
        // e.g., userManager.createSuperAdmin();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (command.trim().equals("quit")) {
                break;
            }
            commandProcessor.process(command);
        }
    }

    // ==========================================
    // getters and setters for CommandProcessor
    // ==========================================
    public RoomManager getRoomManager() {
        return roomManager;
    }

    public UserManager getUserManager() {
        return userManager;
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
}
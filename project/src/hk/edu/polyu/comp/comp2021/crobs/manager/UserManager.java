package hk.edu.polyu.comp.comp2021.crobs.manager;

import hk.edu.polyu.comp.comp2021.crobs.exception.PermissionDeniedException;
import hk.edu.polyu.comp.comp2021.crobs.exception.UserNotFoundException;
import hk.edu.polyu.comp.comp2021.crobs.model.enums.UserRole;
import hk.edu.polyu.comp.comp2021.crobs.model.user.*;
import hk.edu.polyu.comp.comp2021.crobs.util.IDGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserManager {
    private final Map<String, User> users;
    //BookingManager is required for deleting user when that user has a booking
    private final BookingManager bookingManager;
    private User currentUser;

    //easy permission checking
    private static final Map<UserRole, Integer> ROLE_RANK = Map.of(
            UserRole.REGULAR_USER, 0,
            UserRole.ADMIN_USER, 1,
            UserRole.SUPERADMIN, 2
    );

    public UserManager(BookingManager bookingManager) {
        this.users = new HashMap<>();
        this.bookingManager = Objects.requireNonNull(bookingManager, "BookingManager is required");

        //forced SuperAdmin as U000
        SuperAdmin superAdmin = new SuperAdmin("U000", "SuperAdmin");
        users.put(superAdmin.getUserId(), superAdmin);
    }

    public void login(String userId) throws UserNotFoundException {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " does not exist.");
        }
        this.currentUser = user;
        System.out.println("Logged in as " + user.getName() + " (" + user.getRole() + ")");
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Logged out " + currentUser.getName());
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public User addUser(String name)
            throws PermissionDeniedException {

        checkPermission(UserRole.ADMIN_USER);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "User name cannot be empty.");
        }

        String newId =
                IDGenerator.generateUserId();

        RegularUser newUser =
                new RegularUser(
                        newId,
                        name
                );

        users.put(
                newId,
                newUser
        );

        System.out.println(
                "Success: Added user "
                        + name
                        + " with ID "
                        + newId);

        return newUser;
    }

    public void removeUser(String targetUserID) throws PermissionDeniedException, UserNotFoundException {
        checkPermission(UserRole.ADMIN_USER);

        User target = users.get(targetUserID);
        if (target == null) {
            throw new UserNotFoundException("User with ID " + targetUserID + " does not exist.");
        }

        if (target.getRole() != UserRole.REGULAR_USER) {
            throw new PermissionDeniedException("Cannot remove a user who is an Admin or SuperAdmin.");
        }

        bookingManager.cancelBookingsForUser(targetUserID);

        users.remove(targetUserID);
        System.out.println("Success: Removed user " + targetUserID + " and cancelled their bookings.");
    }

    public void changeRole(String targetUserID, String newRoleStr)
            throws PermissionDeniedException, UserNotFoundException, IllegalArgumentException {

        checkPermission(UserRole.SUPERADMIN);

        User target = users.get(targetUserID);
        if (target == null) {
            throw new UserNotFoundException("User with ID " + targetUserID + " does not exist.");
        }

        UserRole newRole = parseRole(newRoleStr);

        User updatedUser = getUpdatedUser(newRole, target);

        users.put(targetUserID, updatedUser);

        //user change its own role
        if (currentUser != null && currentUser.getUserId().equals(targetUserID)) {
            currentUser = updatedUser;
        }

        System.out.println("Success: Changed role of " + targetUserID + " to " + newRole);
    }

    //helper

    public User getCurrentUser() {
        return currentUser;
    }
    /**
     * Returns the user with the specified ID.
     * Used by BON2 command classes.
     */
    public User getUser(String userId) {
        return users.get(userId);
    }
    /**
     * Restores a previously deleted user.
     * Used by undo operations.
     */
    public void restoreUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "Cannot restore a null user.");
        }

        users.put(
                user.getUserId(),
                user
        );
    }
    private void checkPermission(UserRole requiredRole) throws PermissionDeniedException {
        if (currentUser == null) {
            throw new PermissionDeniedException("You must be logged in to perform this action.");
        }

        int userRank = ROLE_RANK.get(currentUser.getRole());
        int requiredRank = ROLE_RANK.get(requiredRole);

        if (userRank < requiredRank) {
            throw new PermissionDeniedException("Permission denied. Requires role: " + requiredRole);
        }
    }

    private UserRole parseRole(String roleStr) {
        String normalized = roleStr.trim().toUpperCase().replaceAll("[_\\s]+", "");
        switch (normalized) {
            case "REGULARUSER": return UserRole.REGULAR_USER;
            case "ADMINUSER": return UserRole.ADMIN_USER;
            case "SUPERADMIN": return UserRole.SUPERADMIN;
            default: throw new IllegalArgumentException("Invalid role: " + roleStr);
        }
    }

    private static User getUpdatedUser(UserRole newRole, User target) {
        User updatedUser;
        if (newRole == UserRole.REGULAR_USER) {
            updatedUser = new RegularUser(target.getUserId(), target.getName());
        } else if (newRole == UserRole.ADMIN_USER) {
            updatedUser = new AdminUser(target.getUserId(), target.getName());
        } else if (newRole == UserRole.SUPERADMIN) {
            updatedUser = new SuperAdmin(target.getUserId(), target.getName());
        } else {
            throw new IllegalArgumentException("Unhandled role type.");
        }
        return updatedUser;
    }
}
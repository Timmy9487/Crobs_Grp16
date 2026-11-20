package hk.edu.polyu.comp.comp2021.crobs.model;

import hk.edu.polyu.comp.comp2021.crobs.exception.PermissionDeniedException;
import hk.edu.polyu.comp.comp2021.crobs.exception.UserNotFoundException;
import hk.edu.polyu.comp.comp2021.crobs.manager.BookingManager;
import hk.edu.polyu.comp.comp2021.crobs.manager.UserManager;
import hk.edu.polyu.comp.comp2021.crobs.model.enums.UserRole;
import hk.edu.polyu.comp.comp2021.crobs.util.IDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    private UserManager userManager;
    private BookingManager bookingManager;

    @BeforeEach
    void setUp() {
        IDGenerator.reset(); // Reset IDs so U001 is always U001
        bookingManager = new BookingManager();
        userManager = new UserManager(bookingManager);
    }

    @Test
    void testInitialSuperAdminCreated() throws UserNotFoundException {
        // The system should create U000 automatically
        userManager.login("U000");
        assertNotNull(userManager.getCurrentUser());
        assertEquals(UserRole.SUPERADMIN, userManager.getCurrentUser().getRole());
    }

    @Test
    void testLoginFailure() {
        assertThrows(UserNotFoundException.class, () -> userManager.login("U999"));
    }

    @Test
    void testLogout() throws UserNotFoundException {
        userManager.login("U000");
        assertNotNull(userManager.getCurrentUser());
        userManager.logout();
        assertNull(userManager.getCurrentUser());

        // Test logging out when already logged out
        userManager.logout();
    }

    @Test
    void testAddUserSuccess() throws Exception {
        userManager.login("U000"); // Login as SuperAdmin
        userManager.addUser("Alice");

        // Alice should be U001
        userManager.logout();
        userManager.login("U001");
        assertEquals("Alice", userManager.getCurrentUser().getName());
        assertEquals(UserRole.REGULAR_USER, userManager.getCurrentUser().getRole());
    }

    @Test
    void testAddUserPermissionDenied() {
        // No one is logged in
        assertThrows(PermissionDeniedException.class, () -> userManager.addUser("Bob"));
    }

    @Test
    void testAddUserEmptyName() throws Exception {
        userManager.login("U000");
        // Test the IllegalArgumentException for empty name
        assertThrows(IllegalArgumentException.class, () -> userManager.addUser("   "));
    }

    @Test
    void testRemoveUserSuccess() throws Exception {
        userManager.login("U000");
        userManager.addUser("Charlie"); // U001

        userManager.removeUser("U001");

        // Charlie should no longer exist
        assertThrows(UserNotFoundException.class, () -> userManager.login("U001"));
    }

    @Test
    void testRemoveUserNotFound() throws Exception {
        userManager.login("U000");
        assertThrows(UserNotFoundException.class, () -> userManager.removeUser("U999"));
    }

    @Test
    void testRemoveAdminDenied() throws Exception {
        userManager.login("U000");
        userManager.addUser("Dave"); // U001
        userManager.changeRole("U001", "ADMIN_USER"); // Make Dave an Admin

        // SuperAdmin tries to remove an Admin
        assertThrows(PermissionDeniedException.class, () -> userManager.removeUser("U001"));
    }

    @Test
    void testChangeRoleSuccess() throws Exception {
        userManager.login("U000");
        userManager.addUser("Eve"); // U001

        // Test all role parsing variations
        userManager.changeRole("U001", "ADMIN_USER");
        userManager.changeRole("U001", "adminuser");
        userManager.changeRole("U001", "Admin_User");
        userManager.changeRole("U001", "SUPERADMIN");

        userManager.logout();
        userManager.login("U001");
        assertEquals(UserRole.SUPERADMIN, userManager.getCurrentUser().getRole());
    }

    @Test
    void testChangeRoleInvalidRole() throws Exception {
        userManager.login("U000");
        userManager.addUser("Frank"); // U001

        // Try to change to a role that doesn't exist
        assertThrows(IllegalArgumentException.class, () -> userManager.changeRole("U001", "KING"));
    }

    @Test
    void testChangeRolePermissionDenied() throws Exception {
        userManager.login("U000");
        userManager.addUser("Grace"); // U001
        userManager.logout();
        userManager.login("U001"); // Login as RegularUser

        // Regular user tries to change a role
        assertThrows(PermissionDeniedException.class, () -> userManager.changeRole("U000", "REGULAR_USER"));
    }

    @Test
    void testSelfDemotion() throws Exception {
        userManager.login("U000"); // Login as SuperAdmin

        // Change own role to Admin
        userManager.changeRole("U000", "ADMIN_USER");

        // Current user should be updated in memory to Admin
        assertEquals(UserRole.ADMIN_USER, userManager.getCurrentUser().getRole());
    }
}

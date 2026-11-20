package hk.edu.polyu.comp.comp2021.crobs.model.user;

import hk.edu.polyu.comp.comp2021.crobs.model.enums.UserRole;

public class AdminUser extends User {

    public AdminUser(String userId, String name) {
        super(userId, name, UserRole.ADMIN_USER);
    }

    protected AdminUser(String userId, String name, UserRole role) {
        super(userId, name, role);
    }
}
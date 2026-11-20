package hk.edu.polyu.comp.comp2021.crobs.model.user;

import hk.edu.polyu.comp.comp2021.crobs.model.enums.UserRole;

public class SuperAdmin extends AdminUser {
    public SuperAdmin(String userId, String name) {
        super(userId, name, UserRole.SUPERADMIN);
    }
}
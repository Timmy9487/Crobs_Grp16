package hk.edu.polyu.comp.comp2021.crobs.model.user;

import hk.edu.polyu.comp.comp2021.crobs.model.enums.UserRole;

public class RegularUser extends User {
    public RegularUser(String userId, String name) {
        super(userId, name, UserRole.REGULAR_USER);
    }
}

package hk.edu.polyu.comp.comp2021.crobs.model.user;

import hk.edu.polyu.comp.comp2021.crobs.model.enums.UserRole;

public abstract class User {
    private final String userId;
    private final String name;
    private final UserRole role;

    public User(String userId, String name, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public UserRole getRole() { return role; }

    @Override
    public String toString() {
        return userId + " - " + name + " (" + role + ")";
    }
}

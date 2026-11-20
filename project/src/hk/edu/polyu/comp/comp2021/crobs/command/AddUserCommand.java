package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.manager.UserManager;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

public class AddUserCommand implements Command {

    private final UserManager userManager;

    private final String name;

    private User createdUser;

    public AddUserCommand(
            UserManager userManager,
            String name) {

        this.userManager = userManager;
        this.name = name;
    }

    @Override
    public void execute() {

        try {

            createdUser =
                    userManager.addUser(name);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to add user.",
                    e
            );
        }
    }

    @Override
    public void undo() {

        if (createdUser == null) {
            return;
        }

        try {

            userManager.removeUser(
                    createdUser.getUserId()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to undo addUser.",
                    e
            );
        }
    }
}

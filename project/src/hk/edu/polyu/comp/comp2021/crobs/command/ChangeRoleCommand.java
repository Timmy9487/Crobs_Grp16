package hk.edu.polyu.comp.comp2021.crobs.command;

import hk.edu.polyu.comp.comp2021.crobs.manager.UserManager;
import hk.edu.polyu.comp.comp2021.crobs.model.enums.UserRole;
import hk.edu.polyu.comp.comp2021.crobs.model.user.User;

public class ChangeRoleCommand
        implements Command {

    private final UserManager userManager;

    private final String userId;

    private final String newRole;

    private UserRole oldRole;

    public ChangeRoleCommand(
            UserManager userManager,
            String userId,
            String newRole) {

        this.userManager = userManager;

        this.userId = userId;

        this.newRole = newRole;
    }

    @Override
    public void execute() {

        User user =
                userManager.getUser(userId);

        oldRole = user.getRole();

        try {

            userManager.changeRole(
                    userId,
                    newRole
            );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public void undo() {

        try {

            userManager.changeRole(
                    userId,
                    oldRole.name()
            );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}

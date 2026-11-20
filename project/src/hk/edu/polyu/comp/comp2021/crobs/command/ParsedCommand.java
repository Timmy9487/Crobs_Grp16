package hk.edu.polyu.comp.comp2021.crobs.command;

/**
 * Immutable representation of a parsed command.
 *
 * Example:
 *
 * Input:
 *   addRoom seminarRoom 40 projector
 *
 * ParsedCommand:
 *   commandName = "addRoom"
 *   arguments = {"seminarRoom", "40", "projector"}
 */
public class ParsedCommand {

    private final String commandName;

    private final String[] arguments;

    public ParsedCommand(
            String commandName,
            String[] arguments) {

        this.commandName = commandName;
        this.arguments = arguments;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArguments() {
        return arguments;
    }
}


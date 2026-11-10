
package hk.edu.polyu.comp.comp2021.crobs.command;

/*Store the parsed command name
+
Store its arguments*/
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

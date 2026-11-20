package hk.edu.polyu.comp.comp2021.crobs.command;

import java.util.Arrays;

/**
 * Converts raw user input into a ParsedCommand.
 */
public class CommandParser {

    public ParsedCommand parse(String input) {

        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Command cannot be empty."
            );
        }

        input = input.trim();

        String[] tokens =
                input.split("\\s+");

        String commandName =
                tokens[0];

        String[] arguments =
                Arrays.copyOfRange(
                        tokens,
                        1,
                        tokens.length
                );

        return new ParsedCommand(
                commandName,
                arguments
        );
    }
}

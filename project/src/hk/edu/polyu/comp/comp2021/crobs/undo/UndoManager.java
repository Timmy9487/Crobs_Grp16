package hk.edu.polyu.comp.comp2021.crobs.undo;

import hk.edu.polyu.comp.comp2021.crobs.command.Command;

import java.util.Stack;

/**
 * BON2 Undo/Redo Manager.
 *
 * Responsibilities:
 * - Execute undoable commands
 * - Maintain undo history
 * - Maintain redo history
 * - Support undo()
 * - Support redo()
 */
public class UndoManager {

    private final Stack<Command> undoStack;

    private final Stack<Command> redoStack;

    public UndoManager() {

        undoStack = new Stack<>();

        redoStack = new Stack<>();
    }

    /**
     * Executes a command and stores it in the undo history.
     *
     * Any new command clears the redo history.
     *
     * @param command command to execute
     */
    public void executeCommand(Command command) {

        if (command == null) {
            throw new IllegalArgumentException(
                    "Command cannot be null."
            );
        }

        command.execute();

        undoStack.push(command);

        redoStack.clear();
    }

    /**
     * Undoes the most recent command.
     */
    public void undo() {

        if (undoStack.isEmpty()) {

            System.out.println(
                    "Nothing to undo."
            );

            return;
        }

        Command command =
                undoStack.pop();

        command.undo();

        redoStack.push(command);

        System.out.println(
                "Undo completed."
        );
    }

    /**
     * Redoes the most recently undone command.
     */
    public void redo() {

        if (redoStack.isEmpty()) {

            System.out.println(
                    "Nothing to redo."
            );

            return;
        }

        Command command =
                redoStack.pop();

        command.execute();

        undoStack.push(command);

        System.out.println(
                "Redo completed."
        );
    }

    /**
     * Returns true if undo is possible.
     */
    public boolean canUndo() {

        return !undoStack.isEmpty();
    }

    /**
     * Returns true if redo is possible.
     */
    public boolean canRedo() {

        return !redoStack.isEmpty();
    }

    /**
     * Returns current undo history size.
     */
    public int getUndoCount() {

        return undoStack.size();
    }

    /**
     * Returns current redo history size.
     */
    public int getRedoCount() {

        return redoStack.size();
    }

    /**
     * Clears all undo/redo history.
     */
    public void clearHistory() {

        undoStack.clear();

        redoStack.clear();
    }
}


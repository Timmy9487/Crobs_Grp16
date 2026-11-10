package hk.edu.polyu.comp.comp2021.crobs.undo;

import hk.edu.polyu.comp.comp2021.crobs.command.Command;

import java.util.Stack;

public class UndoManager {

    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    public UndoManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    /**
     * Execute a new command.
     */
    public void executeCommand(Command command) {

        command.execute();

        undoStack.push(command);

        redoStack.clear();
    }

    /**
     * Undo previous command.
     */
    public void undo() {

        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        Command command = undoStack.pop();

        command.undo();

        redoStack.push(command);
    }

    /**
     * Redo previous command.
     */
    public void redo() {

        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo.");
            return;
        }

        Command command = redoStack.pop();

        command.execute();

        undoStack.push(command);
    }
}

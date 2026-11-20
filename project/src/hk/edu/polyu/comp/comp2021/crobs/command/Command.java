package hk.edu.polyu.comp.comp2021.crobs.command;

/**
 * BON2 Command Pattern
 *
 * Every undoable operation implements this interface.
 *
 * execute():
 *      Performs the operation.
 *
 * undo():
 *      Reverses the operation.
 */
public interface Command {

    /**
     * Execute the command.
     */
    void execute();

    /**
     * Undo the command.
     */
    void undo();
}

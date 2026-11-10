package hk.edu.polyu.comp.comp2021.crobs.command;

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

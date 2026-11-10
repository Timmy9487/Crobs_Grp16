package hk.edu.polyu.comp.comp2021.crobs.logging;

/**
 * Common interface for all loggers.
 */
public interface Logger {

    /**
     * Records a command.
     *
     * @param command the command executed
     */
    void log(String command);

    /**
     * Closes the logger and releases resources.
     */
    void close();
}

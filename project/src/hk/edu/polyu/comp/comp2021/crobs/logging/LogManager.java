package hk.edu.polyu.comp.comp2021.crobs.logging;

/**
 * Handles logging to all configured loggers.
 */
public class LogManager {

    private final Logger htmlLogger;
    private final Logger txtLogger;

    public LogManager(
            Logger htmlLogger,
            Logger txtLogger) {

        this.htmlLogger = htmlLogger;
        this.txtLogger = txtLogger;
    }

    /**
     * Logs a command to all loggers.
     */
    public void log(String command) {

        htmlLogger.log(command);
        txtLogger.log(command);
    }

    /**
     * Closes all loggers.
     */
    public void close() {

        htmlLogger.close();
        txtLogger.close();
    }
}

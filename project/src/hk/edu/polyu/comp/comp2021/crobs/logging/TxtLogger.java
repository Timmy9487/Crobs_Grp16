package hk.edu.polyu.comp.comp2021.crobs.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Writes commands to a text file.
 */
public class TxtLogger implements Logger {

    private final PrintWriter writer;

    public TxtLogger(String filePath) throws IOException {
        writer = new PrintWriter(new FileWriter(filePath));
    }

    @Override
    public void log(String command) {
        writer.println(command);
        writer.flush();
    }

    @Override
    public void close() {
        writer.close();
    }
}

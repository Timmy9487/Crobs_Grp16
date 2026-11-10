package hk.edu.polyu.comp.comp2021.crobs.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Writes commands to an HTML table.
 */
public class HtmlLogger implements Logger {

    private final PrintWriter writer;
    private int operationIndex;

    public HtmlLogger(String filePath) throws IOException {

        writer = new PrintWriter(new FileWriter(filePath));
        operationIndex = 1;

        initialiseHtml();
    }

    private void initialiseHtml() {

        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>CROBS Log</title>");
        writer.println("</head>");
        writer.println("<body>");

        writer.println("<table border=\"1\">");
        writer.println("<tr>");
        writer.println("<th>Operation Index</th>");
        writer.println("<th>Command</th>");
        writer.println("</tr>");

        writer.flush();
    }

    @Override
    public void log(String command) {

        writer.println("<tr>");
        writer.println("<td>" + operationIndex + "</td>");
        writer.println("<td>" + escapeHtml(command) + "</td>");
        writer.println("</tr>");

        operationIndex++;

        writer.flush();
    }

    @Override
    public void close() {

        writer.println("</table>");
        writer.println("</body>");
        writer.println("</html>");

        writer.flush();
        writer.close();
    }

    /**
     * Escapes HTML special characters.
     */
    private String escapeHtml(String text) {

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}

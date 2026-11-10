package hk.edu.polyu.comp.comp2021.crobs;

import hk.edu.polyu.comp.comp2021.crobs.logging.HtmlLogger;
import hk.edu.polyu.comp.comp2021.crobs.logging.LogManager;
import hk.edu.polyu.comp.comp2021.crobs.logging.TxtLogger;
import hk.edu.polyu.comp.comp2021.crobs.model.CROBS;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {

        String htmlLogPath = null;
        String txtLogPath = null;

        // Parse REQ1 launch arguments
        for (int i = 0; i < args.length; i++) {

            if ("-html".equals(args[i]) && i + 1 < args.length) {
                htmlLogPath = args[++i];
            }

            else if ("-txt".equals(args[i]) && i + 1 < args.length) {
                txtLogPath = args[++i];
            }
        }

        // Basic validation
        if (htmlLogPath == null || txtLogPath == null) {

            System.out.println(
                    "Usage: java Application "
                    + "-html <htmlLogFile> "
                    + "-txt <txtLogFile>"
            );

            return;
        }

        try {

            HtmlLogger htmlLogger =
                    new HtmlLogger(htmlLogPath);

            TxtLogger txtLogger =
                    new TxtLogger(txtLogPath);

            LogManager logManager =
                    new LogManager(
                            htmlLogger,
                            txtLogger
                    );

            CROBS crobs =
                    new CROBS(logManager);

            crobs.start();

        } catch (IOException e) {

            System.out.println(
                    "Failed to create log files."
            );

            e.printStackTrace();
        }
    }
}

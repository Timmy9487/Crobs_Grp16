package hk.edu.polyu.comp.comp2021.crobs;

import hk.edu.polyu.comp.comp2021.crobs.model.CROBS;

public class Application {

    public static void main(String[] args) {

        String htmlLogPath = null;
        String txtLogPath = null;

        // Parse REQ1 launch arguments

        for(int i = 0; i < args.length; i++) {

            if(args[i].equals("-html")) {
                htmlLogPath = args[++i];
            }
            else if(args[i].equals("-txt")) {
                txtLogPath = args[++i];
            }
        }

        CROBS crobs = new CROBS(
                htmlLogPath,
                txtLogPath
        );

        crobs.start();
    }
}

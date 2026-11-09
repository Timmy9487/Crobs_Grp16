package hk.edu.polyu.comp.comp2021.crobs.model;

public class CROBS {

    private UserManager userManager;

    private RoomManager roomManager;

    private BookingManager bookingManager;

    private SearchService searchService;

    private CommandProcessor commandProcessor;

    private User currentUser;

    public CROBS(
            String htmlLogFile,
            String txtLogFile
    ) {

        userManager = new UserManager();

        roomManager = new RoomManager();

        bookingManager = new BookingManager();

        searchService = new SearchService(
                roomManager,
                bookingManager
        );

        commandProcessor =
                new CommandProcessor(this);

        initializeDefaultUsers();
    }

    public void start() {

        Scanner scanner =
                new Scanner(System.in);

        while(true) {

            String command =
                    scanner.nextLine();

            if(command.equals("quit")) {
                break;
            }

            commandProcessor.process(command);
        }
    }
}

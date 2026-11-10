package hk.edu.polyu.comp.comp2021.crobs.util;

public class IDGenerator {
    private static int roomCounter = 1;

    public static synchronized String generateRoomId() {
        return String.format("R%03d", roomCounter++);
    }

    // resets counter (for testing)
    public static synchronized void reset() {
        roomCounter = 1;
    }
}
package gol;

public class golUtilities {
    public static void sleep(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
            // https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
            Thread.currentThread().interrupt();
        }
    }
}

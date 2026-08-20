/**
 * Runs the Koara personal assistant chatbot.
 */
public class Koara {
    public static void main(String[] args) {
        String banner = """
                 _  __  ___      _      ____       _
                | |/ / / _ \\    / \\    |  _ \\     / \\
                | ' / | | | |  / _ \\   | |_) |   / _ \\
                | . \\ | |_| | / ___ \\  |  _ <   / ___ \\
                |_|\\_\\ \\___/ /_/   \\_\\ |_| \\_\\ /_/   \\_\\
                """;
        String horizontalLine = "_".repeat(60);

        System.out.println(horizontalLine);
        System.out.print(banner);
        System.out.println("Hello! I'm Koara.");
        System.out.println("What can I do for you?");
        System.out.println(horizontalLine);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(horizontalLine);
    }
}

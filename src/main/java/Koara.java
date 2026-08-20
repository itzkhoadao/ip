import java.util.Scanner;

// Runs the Koara chatbot.
public class Koara {
    private static final int MAX_TASKS = 100; // no more than 100 tasks
    private static final String LINE_INDENT = "    ";
    private static final String RESPONSE_INDENT = LINE_INDENT + " ";
    private static final String HORIZONTAL_LINE = LINE_INDENT + "_".repeat(60);

    // Starts an interactive session that stores tasks, lists them when the user enters "list", and exits when the user enters "bye"
    public static void main(String[] args) {
        String banner = """
                 _  __  ___      _      ____       _
                | |/ / / _ \\    / \\    |  _ \\     / \\
                | ' / | | | |  / _ \\   | |_) |   / _ \\
                | . \\ | |_| | / ___ \\  |  _ <   / ___ \\
                |_|\\_\\ \\___/ /_/   \\_\\ |_| \\_\\ /_/   \\_\\
                """;
        System.out.println(HORIZONTAL_LINE);
        System.out.print(banner.indent(RESPONSE_INDENT.length()));
        System.out.println(RESPONSE_INDENT + "Hello! I'm Koara.");
        System.out.println(RESPONSE_INDENT + "What can I do for you?");
        System.out.println(HORIZONTAL_LINE);

        String[] tasks = new String[MAX_TASKS];
        int taskCount = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                System.out.println(HORIZONTAL_LINE);

                if (command.equals("bye")) {
                    System.out.println(RESPONSE_INDENT + "Bye. Koara hopes to see you again soon!");
                    System.out.println(HORIZONTAL_LINE);
                    break;
                }

                if (command.equals("list")) {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(RESPONSE_INDENT + (i + 1) + ". " + tasks[i]);
                    }
                } else {
                    tasks[taskCount] = command;
                    taskCount++;
                    System.out.println(RESPONSE_INDENT + "added: " + command);
                }
                System.out.println(HORIZONTAL_LINE);
            }
        }
    }
}

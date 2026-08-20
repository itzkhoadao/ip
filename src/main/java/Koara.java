import java.util.Scanner;


// Runs the Koara chatbot
public class Koara {
    private static final int MAX_TASKS = 100;
    private static final String LINE_INDENT = "    ";
    private static final String RESPONSE_INDENT = LINE_INDENT + " ";
    private static final String HORIZONTAL_LINE = LINE_INDENT + "_".repeat(60);

    // Starts a Koara interactive session for adding, listing, marking, and unmarking tasks
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
        boolean[] taskDone = new boolean[MAX_TASKS]; // array to keep track if done or not
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
                    System.out.println(RESPONSE_INDENT + "Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        String statusIcon = taskDone[i] ? "X" : " ";
                        System.out.println(RESPONSE_INDENT + (i + 1)
                                + ".[" + statusIcon + "] " + tasks[i]);
                    }
                } else if (command.startsWith("mark ")) {
                    int taskIndex = Integer.parseInt(command.substring("mark ".length())) - 1; // get the index of the task being marked
                    taskDone[taskIndex] = true; // mark as done
                    System.out.println(RESPONSE_INDENT + "Nice! I've marked this task as done:");
                    System.out.println(RESPONSE_INDENT + "  [X] " + tasks[taskIndex]);
                } else if (command.startsWith("unmark ")) {
                    int taskIndex = Integer.parseInt(command.substring("unmark ".length())) - 1; // get the index of the task being unmarked
                    taskDone[taskIndex] = false; // mark as not done
                    System.out.println(RESPONSE_INDENT + "OK, I've marked this task as not done yet:");
                    System.out.println(RESPONSE_INDENT + "  [ ] " + tasks[taskIndex]);
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

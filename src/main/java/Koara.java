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

        Task[] tasks = new Task[MAX_TASKS]; // new array of Task objects
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
                        System.out.println(RESPONSE_INDENT + (i + 1) + "." + tasks[i]);
                    }
                } else if (command.startsWith("mark ")) {
                    int taskIndex = Integer.parseInt(command.substring("mark ".length())) - 1; // get the index of the task being marked
                    tasks[taskIndex].markAsDone(); // mark as done
                    System.out.println(RESPONSE_INDENT + "Nice! I've marked this task as done:");
                    System.out.println(RESPONSE_INDENT + "  " + tasks[taskIndex]);
                } else if (command.startsWith("unmark ")) {
                    int taskIndex = Integer.parseInt(command.substring("unmark ".length())) - 1; // get the index of the task being unmarked
                    tasks[taskIndex].markAsNotDone(); // mark as not done
                    System.out.println(RESPONSE_INDENT + "OK, I've marked this task as not done yet:");
                    System.out.println(RESPONSE_INDENT + "  " + tasks[taskIndex]);
                } else {
                    tasks[taskCount] = new Task(command); // add new Task object to list
                    taskCount++;
                    System.out.println(RESPONSE_INDENT + "added: " + command);
                }
                System.out.println(HORIZONTAL_LINE);
            }
        }
    }
}

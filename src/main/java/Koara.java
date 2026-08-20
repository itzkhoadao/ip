import java.util.Scanner;

// Runs the Koara chatbot
public class Koara {
    private static final int MAX_TASKS = 100;
    private static final String LINE_INDENT = "    ";
    private static final String RESPONSE_INDENT = LINE_INDENT + " ";
    private static final String HORIZONTAL_LINE = LINE_INDENT + "_".repeat(60);

    // Starts a Koara interactive session for managing todos, deadlines, and events.
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

                // Main logic block: logically process all types of command
                try {
                    if (command.equals("list")) {
                        System.out.println(RESPONSE_INDENT + "Here are the tasks in your list:");
                        for (int i = 0; i < taskCount; i++) {
                            System.out.println(RESPONSE_INDENT + (i + 1) + "." + tasks[i]);
                        }
                    } else if (command.equals("mark") || command.startsWith("mark ")) {
                        int taskIndex = parseTaskIndex(command, "mark", taskCount); // get the index of the task being marked
                        tasks[taskIndex].markAsDone(); // mark as done
                        System.out.println(RESPONSE_INDENT + "Nice! I've marked this task as done:");
                        System.out.println(RESPONSE_INDENT + "  " + tasks[taskIndex]);
                    } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                        int taskIndex = parseTaskIndex(command, "unmark", taskCount); // get the index of the task being unmarked
                        tasks[taskIndex].markAsNotDone(); // mark as not done
                        System.out.println(RESPONSE_INDENT + "OK, I've marked this task as not done yet:");
                        System.out.println(RESPONSE_INDENT + "  " + tasks[taskIndex]);
                    } else { // related to commands that add new tasks
                        Task task = parseTask(command);
                        if (taskCount >= MAX_TASKS) {
                            throw new KoaraException("Sorry bro!!! The task list is full already.");
                        }
                        tasks[taskCount] = task; // add new Task object to list
                        taskCount++;
                        System.out.println(RESPONSE_INDENT + "Got it. I've added this task:");
                        System.out.println(RESPONSE_INDENT + "  " + task); // print the task in appropriate string format
                        System.out.println(RESPONSE_INDENT + "Now you have " + taskCount + " tasks in the list.");
                    }
                } catch (KoaraException exception) {
                    System.out.println(RESPONSE_INDENT + exception.getMessage());
                }
                System.out.println(HORIZONTAL_LINE);
            }
        }
    }

    // Converts a valid Level 4 task command into a task before implementing inheritance.
    // Date and time information is kept as text as required.
    // Parses and validates a command that creates a todo, deadline, or event.
    private static Task parseTask(String command) throws KoaraException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new KoaraException("Unlucky!!! The description of a todo task cannot be empty. Try typing something more.");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String taskDetails = command.substring("deadline".length()).trim();
            if (taskDetails.isEmpty() || taskDetails.startsWith("/by")) {
                throw new KoaraException("Error error!!! The description of a deadline task cannot be empty. Try typing something more.");
            }
            int bySeparatorIndex = taskDetails.indexOf(" /by "); // find deadline information
            if (bySeparatorIndex < 0) {
                throw new KoaraException("Sorry, this cannot!!! A deadline task needs a /by date or time.");
            }
            String description = taskDetails.substring(0, bySeparatorIndex).trim();
            String by = taskDetails.substring(bySeparatorIndex + " /by ".length()).trim(); // deadline
            if (description.isEmpty()) {
                throw new KoaraException("Error error!!! The description of a deadline task cannot be empty. Try typing something more.");
            }
            if (by.isEmpty()) {
                throw new KoaraException("Sorry, cannot bro!!! A deadline task needs a /by date or time.");
            }
            return new Deadline(description, by);
        }

        // unknown commands
        if (!command.equals("event") && !command.startsWith("event ")) {
            throw new KoaraException("What is that bro!!! Sorry ah, I don't know what that means :-(");
        }

        // else (starts with event)
        String taskDetails = command.substring("event".length()).trim();
        if (taskDetails.isEmpty() || taskDetails.startsWith("/from") || taskDetails.startsWith("/to")) {
            throw new KoaraException("Your bad!!! The description of an event task cannot be empty. Try typing something more.");
        }
        int fromSeparatorIndex = taskDetails.indexOf(" /from "); // find from time
        if (fromSeparatorIndex < 0) {
            throw new KoaraException("This cannot ah!!! An event task needs /from and /to dates or times.");
        }
        int toSeparatorIndex = taskDetails.indexOf(" /to ", fromSeparatorIndex + " /from ".length()); // find to time
        if (toSeparatorIndex < 0) {
            throw new KoaraException("Retry retry!!! An event task needs /from and /to dates or times.");
        }

        String description = taskDetails.substring(0, fromSeparatorIndex).trim();
        String from = taskDetails.substring(fromSeparatorIndex + " /from ".length(), toSeparatorIndex).trim();
        String to = taskDetails.substring(toSeparatorIndex + " /to ".length()).trim();

        if (description.isEmpty()) {
            throw new KoaraException("Your bad!!! The description of an event task cannot be empty. Try typing something more.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new KoaraException("Wrong input!!! An event task needs /from and /to dates or times.");
        }

        return new Event(description, from, to);
    }

    // Validates a task number and converts it from the displayed one-based number to an array index.
    // For example, "mark 2", this function will extract 2, validate the number 2, and return 1 (array index).
    private static int parseTaskIndex(String command, String action, int taskCount) throws KoaraException {
        String taskNumberText = command.substring(action.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new KoaraException("Sorry for the inconvenience!!! Please specify a task number to " + action + ".");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new KoaraException("OOPS!!! The task number must be a whole number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new KoaraException("Are you crazy!!! That task number does not exist.");
        }
        return taskNumber - 1;
    }
}

package koara;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the Koara chatbot.
 */
public class Koara {
    private static final String LINE_INDENT = "    ";
    private static final String RESPONSE_INDENT = LINE_INDENT + " ";
    private static final String HORIZONTAL_LINE = LINE_INDENT + "_".repeat(60);
    private static final Path DATA_FILE_PATH = Path.of("data", "koara.txt");

    /**
     * Starts an interactive session for managing todos, deadlines, and events.
     *
     * @param args Command-line arguments, which are not used.
     * @throws IOException If the task list cannot be saved.
     */
    public static void main(String[] args) throws IOException {
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

        ArrayList<Task> tasks = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                System.out.println(HORIZONTAL_LINE);

                if (command.equals("bye")) {
                    System.out.println(RESPONSE_INDENT + "Bye. Koara hopes to see you again soon!");
                    System.out.println(HORIZONTAL_LINE);
                    break;
                }

                try {
                    if (command.equals("list")) {
                        System.out.println(RESPONSE_INDENT + "Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println(RESPONSE_INDENT + (i + 1) + "." + tasks.get(i));
                        }
                    } else if (command.equals("mark") || command.startsWith("mark ")) {
                        int taskIndex = parseTaskIndex(command, "mark", tasks.size());
                        tasks.get(taskIndex).markAsDone();
                        saveTasks(tasks);
                        System.out.println(RESPONSE_INDENT + "Nice! I've marked this task as done:");
                        System.out.println(RESPONSE_INDENT + "  " + tasks.get(taskIndex));
                    } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                        int taskIndex = parseTaskIndex(command, "unmark", tasks.size());
                        tasks.get(taskIndex).markAsNotDone();
                        saveTasks(tasks);
                        System.out.println(RESPONSE_INDENT + "OK, I've marked this task as not done yet:");
                        System.out.println(RESPONSE_INDENT + "  " + tasks.get(taskIndex));
                    } else if (command.equals("delete") || command.startsWith("delete ")) {
                        int taskIndex = parseTaskIndex(command, "delete", tasks.size());
                        Task removedTask = tasks.remove(taskIndex);
                        saveTasks(tasks);
                        System.out.println(RESPONSE_INDENT + "Noted. I've removed this task:");
                        System.out.println(RESPONSE_INDENT + "  " + removedTask);
                        System.out.println(RESPONSE_INDENT + "Now you have " + tasks.size() + " tasks in the list.");
                    } else {
                        Task task = parseTask(command);
                        tasks.add(task);
                        saveTasks(tasks);
                        System.out.println(RESPONSE_INDENT + "Got it. I've added this task:");
                        System.out.println(RESPONSE_INDENT + "  " + task);
                        System.out.println(RESPONSE_INDENT + "Now you have " + tasks.size() + " tasks in the list.");
                    }
                } catch (KoaraException exception) {
                    System.out.println(RESPONSE_INDENT + exception.getMessage());
                }
                System.out.println(HORIZONTAL_LINE);
            }
        }
    }

    /**
     * Saves the complete task list using Koara's line-based data format.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the task list cannot be written.
     */
    private static void saveTasks(ArrayList<Task> tasks) throws IOException {
        ArrayList<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            taskLines.add(task.toDataString());
        }
        Files.write(DATA_FILE_PATH, taskLines);
    }

    /**
     * Parses and validates a command that creates a task.
     *
     * @param command Command entered by the user.
     * @return Task represented by the command.
     * @throws KoaraException If the command is invalid.
     */
    private static Task parseTask(String command) throws KoaraException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new KoaraException("Unlucky!!! The description of a todo task cannot be empty. "
                        + "Try typing something more.");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String taskDetails = command.substring("deadline".length()).trim();
            if (taskDetails.isEmpty() || taskDetails.startsWith("/by")) {
                throw new KoaraException("Error error!!! The description of a deadline task cannot be empty. "
                        + "Try typing something more.");
            }
            int bySeparatorIndex = taskDetails.indexOf(" /by ");
            if (bySeparatorIndex < 0) {
                throw new KoaraException("Sorry, this cannot!!! A deadline task needs a /by date or time.");
            }
            String description = taskDetails.substring(0, bySeparatorIndex).trim();
            String by = taskDetails.substring(bySeparatorIndex + " /by ".length()).trim();
            if (description.isEmpty()) {
                throw new KoaraException("Error error!!! The description of a deadline task cannot be empty. "
                        + "Try typing something more.");
            }
            if (by.isEmpty()) {
                throw new KoaraException("Sorry, cannot bro!!! A deadline task needs a /by date or time.");
            }
            return new Deadline(description, by);
        }

        if (!command.equals("event") && !command.startsWith("event ")) {
            throw new KoaraException("What is that bro!!! Sorry ah, I don't know what that means :-(");
        }

        String taskDetails = command.substring("event".length()).trim();
        if (taskDetails.isEmpty() || taskDetails.startsWith("/from") || taskDetails.startsWith("/to")) {
            throw new KoaraException("Your bad!!! The description of an event task cannot be empty. "
                    + "Try typing something more.");
        }
        int fromSeparatorIndex = taskDetails.indexOf(" /from ");
        if (fromSeparatorIndex < 0) {
            throw new KoaraException("This cannot ah!!! An event task needs /from and /to dates or times.");
        }
        int toSeparatorIndex = taskDetails.indexOf(" /to ",
                fromSeparatorIndex + " /from ".length());
        if (toSeparatorIndex < 0) {
            throw new KoaraException("Retry retry!!! An event task needs /from and /to dates or times.");
        }

        String description = taskDetails.substring(0, fromSeparatorIndex).trim();
        String from = taskDetails.substring(fromSeparatorIndex + " /from ".length(), toSeparatorIndex).trim();
        String to = taskDetails.substring(toSeparatorIndex + " /to ".length()).trim();

        if (description.isEmpty()) {
            throw new KoaraException("Your bad!!! The description of an event task cannot be empty. "
                    + "Try typing something more.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new KoaraException("Wrong input!!! An event task needs /from and /to dates or times.");
        }

        return new Event(description, from, to);
    }

    /**
     * Validates a displayed task number and converts it to a zero-based index.
     *
     * @param command Command containing the task number.
     * @param action Action being performed on the task.
     * @param taskCount Number of tasks in the list.
     * @return Zero-based index of the selected task.
     * @throws KoaraException If the task number is missing or invalid.
     */
    private static int parseTaskIndex(String command, String action, int taskCount) throws KoaraException {
        String taskNumberText = command.substring(action.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new KoaraException("Sorry for the inconvenience!!! Please specify a task number to "
                    + action + ".");
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

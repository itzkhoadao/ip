package koara.ui;

import java.util.Scanner;

import koara.task.Task;
import koara.task.TaskList;

/**
 * Handles interactions between Koara and the user.
 */
public class Ui implements AutoCloseable {
    private static final String LINE_INDENT = "    ";
    private static final String RESPONSE_INDENT = LINE_INDENT + " ";
    private static final String HORIZONTAL_LINE = LINE_INDENT + "_".repeat(60);

    private final Scanner scanner;

    /**
     * Creates a UI that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays Koara's greeting.
     */
    public void showWelcome() {
        String banner = """
                 _  __  ___      _      ____       _
                | |/ / / _ \\    / \\    |  _ \\     / \\
                | ' / | | | |  / _ \\   | |_) |   / _ \\
                | . \\ | |_| | / ___ \\  |  _ <   / ___ \\
                |_|\\_\\ \\___/ /_/   \\_\\ |_| \\_\\ /_/   \\_\\
                """;
        showLine();
        System.out.print(banner.indent(RESPONSE_INDENT.length()));
        showMessage("Hello! I'm Koara.");
        showMessage("What can I do for you?");
        showLine();
    }

    /**
     * Returns whether another command is available.
     *
     * @return True if another command can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return Next command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the horizontal response divider.
     */
    public void showLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays Koara's farewell.
     */
    public void showGoodbye() {
        showMessage("Bye. Koara hopes to see you again soon!");
    }

    /**
     * Displays all tasks in their current order.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task Task that was marked.
     */
    public void showTaskMarked(Task task) {
        showMessage("Nice! I've marked this task as done:");
        showMessage("  " + task);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task Task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        showMessage("OK, I've marked this task as not done yet:");
        showMessage("  " + task);
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task Task that was deleted.
     * @param taskCount Number of tasks remaining.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showMessage("Noted. I've removed this task:");
        showMessage("  " + task);
        showMessage("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks in the list.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Got it. I've added this task:");
        showMessage("  " + task);
        showMessage("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays an error message.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        showMessage(message);
    }

    private void showMessage(String message) {
        System.out.println(RESPONSE_INDENT + message);
    }

    /**
     * Closes the scanner used to read commands.
     */
    @Override
    public void close() {
        scanner.close();
    }
}

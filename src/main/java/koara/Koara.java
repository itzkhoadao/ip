package koara;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import koara.exception.KoaraException;
import koara.parser.Parser;
import koara.storage.Storage;
import koara.task.Task;
import koara.task.TaskList;
import koara.ui.Ui;

/**
 * Coordinates Koara's user interface, task list, parser, and storage.
 */
public class Koara {
    private static final Path DATA_FILE_PATH = Path.of("data", "koara.txt");

    private final Storage storage;
    private final TaskList tasks;
    private final String startupError;

    /**
     * Creates Koara using its default data file.
     */
    public Koara() {
        this(DATA_FILE_PATH);
    }

    /**
     * Creates Koara using the specified data file.
     *
     * @param dataFilePath Path of the data file.
     */
    public Koara(Path dataFilePath) {
        assert dataFilePath != null : "Data file path must not be null";
        storage = new Storage(dataFilePath);
        TaskList loadedTasks;
        String loadError = null;
        try {
            loadedTasks = storage.load();
        } catch (KoaraException exception) {
            loadedTasks = new TaskList();
            loadError = exception.getMessage();
        }
        tasks = loadedTasks;
        startupError = loadError;
    }

    /**
     * Starts an interactive session for managing tasks.
     */
    public void run() {
        try (Ui ui = new Ui()) {
            ui.showWelcome();
            if (startupError != null) {
                ui.showResponse(startupError);
                ui.showLine();
            }
            while (ui.hasNextCommand()) {
                String command = ui.readCommand();
                ui.showLine();
                ui.showResponse(getResponse(command));
                ui.showLine();
                if (command.equals("bye")) {
                    break;
                }
            }
        }
    }

    /**
     * Returns an initialization error that should be shown to the user, if any.
     *
     * @return Initialization error, or an empty value when loading succeeded.
     */
    public Optional<String> getStartupError() {
        return Optional.ofNullable(startupError);
    }

    /**
     * Executes a command and returns Koara's response.
     *
     * @param command Command entered by the user.
     * @return User-facing response to the command.
     */
    public String getResponse(String command) {
        assert command != null : "Command must not be null";
        if (command.equals("bye")) {
            return "Bye. Koara hopes to see you again soon!";
        }

        try {
            return executeCommand(command);
        } catch (KoaraException exception) {
            return exception.getMessage();
        }
    }

    /**
     * Executes a non-exit command and persists any task-list changes.
     *
     * @param command Command entered by the user.
     * @return User-facing response to the command.
     * @throws KoaraException If the command is invalid or a change cannot be saved.
     */
    private String executeCommand(String command) throws KoaraException {
        assert command != null : "Command must not be null";
        assert !command.equals("bye") : "Exit command must be handled before execution";
        if (command.equals("list")) {
            return formatTaskList("Here are the tasks in your list:", tasks);
        }
        if (Parser.matchesCommand(command, "mark")) {
            int taskIndex = Parser.parseTaskIndex(command, "mark", tasks.getSize());
            tasks.mark(taskIndex);
            storage.save(tasks);
            return "Nice! I've marked this task as done:\n  " + tasks.get(taskIndex);
        }
        if (Parser.matchesCommand(command, "unmark")) {
            int taskIndex = Parser.parseTaskIndex(command, "unmark", tasks.getSize());
            tasks.unmark(taskIndex);
            storage.save(tasks);
            return "OK, I've marked this task as not done yet:\n  " + tasks.get(taskIndex);
        }
        if (Parser.matchesCommand(command, "delete")) {
            int taskIndex = Parser.parseTaskIndex(command, "delete", tasks.getSize());
            Task removedTask = tasks.delete(taskIndex);
            storage.save(tasks);
            return "Noted. I've removed this task:\n  " + removedTask
                    + "\nNow you have " + tasks.getSize() + " tasks in the list.";
        }
        if (Parser.matchesCommand(command, "find")) {
            String keyword = Parser.parseFindKeyword(command);
            return formatTaskList("Here are the matching tasks in your list:",
                    tasks.find(keyword));
        }

        Task task = Parser.parseTask(command);
        tasks.add(task);
        storage.save(tasks);
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + tasks.getSize() + " tasks in the list.";
    }

    /**
     * Formats a heading followed by a numbered list of tasks.
     *
     * @param heading Heading placed before the tasks.
     * @param taskList Tasks to format.
     * @return Formatted task-list response.
     */
    private static String formatTaskList(String heading, TaskList taskList) {
        assert heading != null : "Task list heading must not be null";
        assert taskList != null : "Task list must not be null";
        String formattedTasks = IntStream.range(0, taskList.getSize())
                .mapToObj(index -> "\n" + (index + 1) + "." + taskList.get(index))
                .collect(Collectors.joining());
        return heading + formattedTasks;
    }

    /**
     * Starts Koara using its default data file.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Koara(DATA_FILE_PATH).run();
    }
}

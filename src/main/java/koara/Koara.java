package koara;

import java.nio.file.Path;
import java.util.Optional;

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
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
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
                if (command.equals(EXIT_COMMAND)) {
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
        if (command.equals(EXIT_COMMAND)) {
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
        if (command.equals(LIST_COMMAND)) {
            return formatTaskList("Here are the tasks in your list:", tasks);
        }
        if (Parser.matchesCommand(command, MARK_COMMAND)) {
            return markTask(command);
        }
        if (Parser.matchesCommand(command, UNMARK_COMMAND)) {
            return unmarkTask(command);
        }
        if (Parser.matchesCommand(command, DELETE_COMMAND)) {
            return deleteTask(command);
        }
        if (Parser.matchesCommand(command, FIND_COMMAND)) {
            return findTasks(command);
        }
        return addTask(command);
    }

    private String markTask(String command) throws KoaraException {
        int taskIndex = Parser.parseTaskIndex(command, MARK_COMMAND, tasks.getSize());
        tasks.mark(taskIndex);
        storage.save(tasks);
        return "Nice! I've marked this task as done:\n  " + tasks.get(taskIndex);
    }

    private String unmarkTask(String command) throws KoaraException {
        int taskIndex = Parser.parseTaskIndex(command, UNMARK_COMMAND, tasks.getSize());
        tasks.unmark(taskIndex);
        storage.save(tasks);
        return "OK, I've marked this task as not done yet:\n  " + tasks.get(taskIndex);
    }

    private String deleteTask(String command) throws KoaraException {
        int taskIndex = Parser.parseTaskIndex(command, DELETE_COMMAND, tasks.getSize());
        Task removedTask = tasks.delete(taskIndex);
        storage.save(tasks);
        return "Noted. I've removed this task:\n  " + removedTask
                + "\nNow you have " + tasks.getSize() + " tasks in the list.";
    }

    private String findTasks(String command) throws KoaraException {
        String keyword = Parser.parseFindKeyword(command);
        TaskList matchingTasks = tasks.find(keyword);
        return formatTaskList("Here are the matching tasks in your list:", matchingTasks);
    }

    private String addTask(String command) throws KoaraException {
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
        StringBuilder response = new StringBuilder(heading);
        for (int i = 0; i < taskList.getSize(); i++) {
            response.append("\n").append(i + 1).append(".").append(taskList.get(i));
        }
        return response.toString();
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

package koara;

import java.nio.file.Path;

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
    private final Ui ui;
    private TaskList tasks;

    /**
     * Creates Koara using the specified data file.
     *
     * @param dataFilePath Path of the data file.
     */
    public Koara(Path dataFilePath) {
        storage = new Storage(dataFilePath);
        ui = new Ui();
        tasks = new TaskList();
    }

    /**
     * Starts an interactive session for managing tasks.
     */
    public void run() {
        ui.showWelcome();
        try {
            tasks = storage.load();
        } catch (KoaraException exception) {
            ui.showError(exception.getMessage());
            ui.showLine();
        }

        try {
            while (ui.hasNextCommand()) {
                String command = ui.readCommand();
                ui.showLine();

                if (command.equals("bye")) {
                    ui.showGoodbye();
                    ui.showLine();
                    break;
                }

                try {
                    executeCommand(command);
                } catch (KoaraException exception) {
                    ui.showError(exception.getMessage());
                }
                ui.showLine();
            }
        } finally {
            ui.close();
        }
    }

    /**
     * Executes a non-exit command and persists any resulting task-list changes.
     *
     * @param command Command entered by the user.
     * @throws KoaraException If the command is invalid or a task-list change cannot be saved.
     */
    private void executeCommand(String command) throws KoaraException {
        if (command.equals("list")) {
            ui.showTaskList(tasks);
        } else if (Parser.matchesCommand(command, "mark")) {
            int taskIndex = Parser.parseTaskIndex(command, "mark", tasks.getSize());
            tasks.mark(taskIndex);
            storage.save(tasks);
            ui.showTaskMarked(tasks.get(taskIndex));
        } else if (Parser.matchesCommand(command, "unmark")) {
            int taskIndex = Parser.parseTaskIndex(command, "unmark", tasks.getSize());
            tasks.unmark(taskIndex);
            storage.save(tasks);
            ui.showTaskUnmarked(tasks.get(taskIndex));
        } else if (Parser.matchesCommand(command, "delete")) {
            int taskIndex = Parser.parseTaskIndex(command, "delete", tasks.getSize());
            Task removedTask = tasks.delete(taskIndex);
            storage.save(tasks);
            ui.showTaskDeleted(removedTask, tasks.getSize());
        } else if (Parser.matchesCommand(command, "find")) {
            String keyword = Parser.parseFindKeyword(command);
            ui.showMatchingTasks(tasks.find(keyword));
        } else {
            Task task = Parser.parseTask(command);
            tasks.add(task);
            storage.save(tasks);
            ui.showTaskAdded(task, tasks.getSize());
        }
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

package koara.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import koara.exception.KoaraException;
import koara.task.Deadline;
import koara.task.Event;
import koara.task.Task;
import koara.task.TaskList;
import koara.task.Todo;

/**
 * Loads tasks from the data file and saves tasks to it.
 */
public class Storage {
    private static final String INVALID_SAVED_DATA_ERROR = "Sorry, the saved task data is invalid.";

    private final Path dataFilePath;

    /**
     * Creates storage that uses the specified data file.
     *
     * @param dataFilePath Path of the data file.
     */
    public Storage(Path dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return Tasks loaded from the data file.
     * @throws KoaraException If the data file cannot be read or contains invalid data.
     */
    public TaskList load() throws KoaraException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (Files.notExists(dataFilePath)) {
            return new TaskList(tasks);
        }

        try {
            for (String taskLine : Files.readAllLines(dataFilePath, StandardCharsets.UTF_8)) {
                tasks.add(parseStoredTask(taskLine));
            }
        } catch (IOException exception) {
            throw new KoaraException("Sorry, I couldn't load your saved tasks.");
        }
        return new TaskList(tasks);
    }

    /**
     * Saves the complete task list to the data file.
     *
     * @param tasks Tasks to save.
     * @throws KoaraException If the task list cannot be written.
     */
    public void save(TaskList tasks) throws KoaraException {
        try {
            Path dataDirectory = dataFilePath.getParent();
            if (dataDirectory != null) {
                Files.createDirectories(dataDirectory);
            }
            Files.write(dataFilePath, tasks.toDataLines(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new KoaraException("Sorry, I couldn't save your tasks.");
        }
    }

    /**
     * Reconstructs a task from one line of saved task data.
     *
     * @param taskLine Saved task data to parse.
     * @return Task represented by the saved data.
     * @throws KoaraException If the saved data is invalid.
     */
    private Task parseStoredTask(String taskLine) throws KoaraException {
        String[] taskParts = taskLine.split(" \\| ", -1);
        if (taskParts.length < 3 || taskParts[2].isEmpty()) {
            throw new KoaraException(INVALID_SAVED_DATA_ERROR);
        }

        Task task;
        switch (taskParts[0]) {
            case "T":
                if (taskParts.length != 3) {
                    throw new KoaraException(INVALID_SAVED_DATA_ERROR);
                }
                task = new Todo(taskParts[2]);
                break;
            case "D":
                if (taskParts.length != 4 || taskParts[3].isEmpty()) {
                    throw new KoaraException(INVALID_SAVED_DATA_ERROR);
                }
                task = new Deadline(taskParts[2], parseDate(taskParts[3]));
                break;
            case "E":
                if (taskParts.length != 5 || taskParts[3].isEmpty() || taskParts[4].isEmpty()) {
                    throw new KoaraException(INVALID_SAVED_DATA_ERROR);
                }
                LocalDate from = parseDate(taskParts[3]);
                LocalDate to = parseDate(taskParts[4]);
                task = new Event(taskParts[2], from, to);
                break;
            default:
                throw new KoaraException(INVALID_SAVED_DATA_ERROR);
        }

        if (taskParts[1].equals("1")) {
            task.markAsDone();
        } else if (!taskParts[1].equals("0")) {
            throw new KoaraException(INVALID_SAVED_DATA_ERROR);
        }
        return task;
    }

    /**
     * Parses a date stored in the data file.
     *
     * @param dateText Stored date text to parse.
     * @return Parsed date.
     * @throws KoaraException If the stored date is invalid.
     */
    private LocalDate parseDate(String dateText) throws KoaraException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException exception) {
            throw new KoaraException(INVALID_SAVED_DATA_ERROR);
        }
    }
}

package koara.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import koara.exception.KoaraException;
import koara.task.Deadline;
import koara.task.Event;
import koara.task.Task;
import koara.task.Todo;

/**
 * Parses and validates commands entered by the user.
 */
public class Parser {
    private static final String DATE_FORMAT_ERROR = "Wrong date format!!! Please use yyyy-MM-dd.";

    private Parser() {
    }

    /**
     * Returns whether a command uses the specified keyword.
     *
     * @param command Command entered by the user.
     * @param keyword Expected command keyword.
     * @return True if the command uses the keyword.
     */
    public static boolean matchesCommand(String command, String keyword) {
        return command.equals(keyword) || command.startsWith(keyword + " ");
    }

    /**
     * Parses and validates a command that creates a task.
     *
     * @param command Command entered by the user.
     * @return Task represented by the command.
     * @throws KoaraException If the command is invalid.
     */
    public static Task parseTask(String command) throws KoaraException {
        if (matchesCommand(command, "todo")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new KoaraException("Unlucky!!! The description of a todo task cannot be empty. "
                        + "Try typing something more.");
            }
            return new Todo(description);
        }

        if (matchesCommand(command, "deadline")) {
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
            String byText = taskDetails.substring(bySeparatorIndex + " /by ".length()).trim();
            if (description.isEmpty()) {
                throw new KoaraException("Error error!!! The description of a deadline task cannot be empty. "
                        + "Try typing something more.");
            }
            if (byText.isEmpty()) {
                throw new KoaraException("Sorry, cannot bro!!! A deadline task needs a /by date or time.");
            }
            return new Deadline(description, parseDate(byText));
        }

        if (!matchesCommand(command, "event")) {
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
        String fromText = taskDetails.substring(fromSeparatorIndex + " /from ".length(), toSeparatorIndex).trim();
        String toText = taskDetails.substring(toSeparatorIndex + " /to ".length()).trim();

        if (description.isEmpty()) {
            throw new KoaraException("Your bad!!! The description of an event task cannot be empty. "
                    + "Try typing something more.");
        }
        if (fromText.isEmpty() || toText.isEmpty()) {
            throw new KoaraException("Wrong input!!! An event task needs /from and /to dates or times.");
        }

        return new Event(description, parseDate(fromText), parseDate(toText));
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
    public static int parseTaskIndex(String command, String action, int taskCount) throws KoaraException {
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

    /**
     * Extracts and validates the keyword from a find command.
     *
     * @param command Find command entered by the user.
     * @return Keyword to search for.
     * @throws KoaraException If the keyword is empty.
     */
    public static String parseFindKeyword(String command) throws KoaraException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new KoaraException("Oops!!! Please specify a keyword to find.");
        }
        return keyword;
    }

    /**
     * Parses a date in the required ISO local-date format.
     *
     * @param dateText Date text to parse.
     * @return Parsed date.
     * @throws KoaraException If the text is not a valid date in yyyy-MM-dd format.
     */
    private static LocalDate parseDate(String dateText) throws KoaraException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException exception) {
            throw new KoaraException(DATE_FORMAT_ERROR);
        }
    }
}

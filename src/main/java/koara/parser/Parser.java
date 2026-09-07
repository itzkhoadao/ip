package koara.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import koara.client.Client;
import koara.exception.KoaraException;
import koara.task.Deadline;
import koara.task.Event;
import koara.task.Task;
import koara.task.Todo;

/**
 * Parses and validates commands entered by the user.
 */
public class Parser {
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String FIND_COMMAND = "find";
    private static final String CLIENT_ADD_COMMAND = "client add";
    private static final String CLIENT_EDIT_COMMAND = "client edit";
    private static final String CLIENT_FIND_COMMAND = "client find";
    private static final String CLIENT_DELETE_COMMAND = "client delete";
    private static final String NAME_SEPARATOR = " /name ";
    private static final String PHONE_SEPARATOR = " /phone ";
    private static final String GOAL_SEPARATOR = " /goal ";
    private static final String NOTES_SEPARATOR = " /notes ";
    private static final String BY_SEPARATOR = " /by ";
    private static final String FROM_SEPARATOR = " /from ";
    private static final String TO_SEPARATOR = " /to ";
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
        assert command != null : "Command must not be null";
        assert keyword != null && !keyword.isBlank() : "Command keyword must not be blank";
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
        assert command != null : "Command must not be null";
        if (matchesCommand(command, TODO_COMMAND)) {
            return parseTodo(command);
        }
        if (matchesCommand(command, DEADLINE_COMMAND)) {
            return parseDeadline(command);
        }
        if (matchesCommand(command, EVENT_COMMAND)) {
            return parseEvent(command);
        }
        throw new KoaraException("What is that bro!!! Sorry ah, I don't know what that means :-(");
    }

    private static Todo parseTodo(String command) throws KoaraException {
        String description = extractTaskDetails(command, TODO_COMMAND);
        if (description.isEmpty()) {
            throw new KoaraException("Unlucky!!! The description of a todo task cannot be empty. "
                    + "Try typing something more.");
        }
        return new Todo(description);
    }

    private static Deadline parseDeadline(String command) throws KoaraException {
        String taskDetails = extractTaskDetails(command, DEADLINE_COMMAND);
        if (taskDetails.isEmpty() || taskDetails.startsWith("/by")) {
            throw new KoaraException("Error error!!! The description of a deadline task cannot be empty. "
                    + "Try typing something more.");
        }

        int bySeparatorIndex = taskDetails.indexOf(BY_SEPARATOR);
        if (bySeparatorIndex < 0) {
            throw new KoaraException("Sorry, this cannot!!! A deadline task needs a /by date or time.");
        }

        String description = taskDetails.substring(0, bySeparatorIndex).trim();
        String dueDateText = taskDetails.substring(bySeparatorIndex + BY_SEPARATOR.length()).trim();
        if (dueDateText.isEmpty()) {
            throw new KoaraException("Sorry, cannot bro!!! A deadline task needs a /by date or time.");
        }
        return new Deadline(description, parseDate(dueDateText));
    }

    private static Event parseEvent(String command) throws KoaraException {
        String taskDetails = extractTaskDetails(command, EVENT_COMMAND);
        validateEventDescription(taskDetails);

        int fromSeparatorIndex = taskDetails.indexOf(FROM_SEPARATOR);
        if (fromSeparatorIndex < 0) {
            throw new KoaraException("This cannot ah!!! An event task needs /from and /to dates or times.");
        }

        int toSeparatorIndex = taskDetails.indexOf(TO_SEPARATOR,
                fromSeparatorIndex + FROM_SEPARATOR.length());
        if (toSeparatorIndex < 0) {
            throw new KoaraException("Retry retry!!! An event task needs /from and /to dates or times.");
        }

        String description = taskDetails.substring(0, fromSeparatorIndex).trim();
        String startDateText = taskDetails.substring(
                fromSeparatorIndex + FROM_SEPARATOR.length(), toSeparatorIndex).trim();
        String endDateText = taskDetails.substring(toSeparatorIndex + TO_SEPARATOR.length()).trim();
        if (startDateText.isEmpty() || endDateText.isEmpty()) {
            throw new KoaraException("Wrong input!!! An event task needs /from and /to dates or times.");
        }
        return new Event(description, parseDate(startDateText), parseDate(endDateText));
    }

    private static void validateEventDescription(String taskDetails) throws KoaraException {
        boolean hasNoDescription = taskDetails.isEmpty()
                || taskDetails.startsWith("/from")
                || taskDetails.startsWith("/to");
        if (hasNoDescription) {
            throw new KoaraException("Your bad!!! The description of an event task cannot be empty. "
                    + "Try typing something more.");
        }
    }

    private static String extractTaskDetails(String command, String commandWord) {
        return command.substring(commandWord.length()).trim();
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
        assert action != null && !action.isBlank() : "Task action must not be blank";
        assert matchesCommand(command, action) : "Command must match the task action";
        assert taskCount >= 0 : "Task count must not be negative";
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
        assert matchesCommand(command, FIND_COMMAND) : "Command must be a find command";
        String keyword = command.substring(FIND_COMMAND.length()).trim();
        if (keyword.isEmpty()) {
            throw new KoaraException("Oops!!! Please specify a keyword to find.");
        }
        return keyword;
    }

    /**
     * Parses a command that creates a client.
     *
     * @param command Client-add command entered by the user.
     * @return Client represented by the command.
     * @throws KoaraException If any required client field is missing.
     */
    public static Client parseClient(String command) throws KoaraException {
        assert matchesCommand(command, CLIENT_ADD_COMMAND) : "Command must add a client";
        String details = command.substring(CLIENT_ADD_COMMAND.length()).trim();
        return parseClientDetails(details, false);
    }

    /**
     * Parses a command that replaces a client's details.
     *
     * @param command Client-edit command entered by the user.
     * @param clientCount Number of clients currently stored.
     * @return Parsed client index and replacement details.
     * @throws KoaraException If the index or client details are invalid.
     */
    public static ClientEdit parseClientEdit(String command, int clientCount) throws KoaraException {
        assert matchesCommand(command, CLIENT_EDIT_COMMAND) : "Command must edit a client";
        int nameSeparatorIndex = command.indexOf(NAME_SEPARATOR);
        if (nameSeparatorIndex < 0) {
            throw clientFormatException(CLIENT_EDIT_COMMAND + " INDEX /name");
        }
        String indexCommand = command.substring(0, nameSeparatorIndex);
        int clientIndex = parseClientIndex(indexCommand, CLIENT_EDIT_COMMAND, clientCount);
        String details = command.substring(nameSeparatorIndex + 1);
        return new ClientEdit(clientIndex, parseClientDetails(details, true));
    }

    /**
     * Parses the keyword from a client-find command.
     *
     * @param command Client-find command entered by the user.
     * @return Keyword to search for.
     * @throws KoaraException If the keyword is empty.
     */
    public static String parseClientKeyword(String command) throws KoaraException {
        assert matchesCommand(command, CLIENT_FIND_COMMAND) : "Command must find clients";
        String keyword = command.substring(CLIENT_FIND_COMMAND.length()).trim();
        if (keyword.isEmpty()) {
            throw new KoaraException("Please specify client information to find.");
        }
        return keyword;
    }

    /**
     * Parses and validates a displayed client number.
     *
     * @param command Command containing the client number.
     * @param action Client action being performed.
     * @param clientCount Number of clients currently stored.
     * @return Zero-based index of the selected client.
     * @throws KoaraException If the client number is missing or invalid.
     */
    public static int parseClientIndex(String command, String action, int clientCount) throws KoaraException {
        assert matchesCommand(command, action) : "Command must match the client action";
        assert clientCount >= 0 : "Client count must not be negative";
        String clientNumberText = command.substring(action.length()).trim();
        if (clientNumberText.isEmpty()) {
            throw new KoaraException("Please specify a client number to " + action.substring("client ".length()) + ".");
        }

        int clientNumber;
        try {
            clientNumber = Integer.parseInt(clientNumberText);
        } catch (NumberFormatException exception) {
            throw new KoaraException("The client number must be a whole number.");
        }
        if (clientNumber < 1 || clientNumber > clientCount) {
            throw new KoaraException("That client number does not exist.");
        }
        return clientNumber - 1;
    }

    private static Client parseClientDetails(String details, boolean includesNameLabel) throws KoaraException {
        String normalizedDetails = details;
        int phoneIndex = normalizedDetails.indexOf(PHONE_SEPARATOR);
        int goalIndex = normalizedDetails.indexOf(GOAL_SEPARATOR);
        int notesIndex = normalizedDetails.indexOf(NOTES_SEPARATOR);
        boolean hasInvalidOrder = phoneIndex < 0 || goalIndex < phoneIndex || notesIndex < goalIndex;
        if (hasInvalidOrder) {
            throw clientFormatException(CLIENT_ADD_COMMAND);
        }

        int nameStartIndex = includesNameLabel ? "/name ".length() : 0;
        String name = normalizedDetails.substring(nameStartIndex, phoneIndex).trim();
        String phone = normalizedDetails.substring(phoneIndex + PHONE_SEPARATOR.length(), goalIndex).trim();
        String goal = normalizedDetails.substring(goalIndex + GOAL_SEPARATOR.length(), notesIndex).trim();
        String notes = normalizedDetails.substring(notesIndex + NOTES_SEPARATOR.length()).trim();
        if (name.isEmpty() || phone.isEmpty() || goal.isEmpty() || notes.isEmpty()) {
            throw clientFormatException(CLIENT_ADD_COMMAND);
        }
        if (containsUnsupportedCharacter(name, phone, goal, notes)) {
            throw new KoaraException("Client details cannot contain tabs or line breaks.");
        }
        return new Client(name, phone, goal, notes);
    }

    private static boolean containsUnsupportedCharacter(String... fields) {
        for (String field : fields) {
            if (field.contains("\t") || field.contains("\n") || field.contains("\r")) {
                return true;
            }
        }
        return false;
    }

    private static KoaraException clientFormatException(String command) {
        return new KoaraException("Use: " + command
                + " NAME /phone PHONE /goal GOAL /notes NOTES");
    }

    /**
     * Contains a parsed client-edit target and its replacement details.
     *
     * @param clientIndex Zero-based client index.
     * @param client Replacement client details.
     */
    public record ClientEdit(int clientIndex, Client client) {
    }

    /**
     * Parses a date in the required ISO local-date format.
     *
     * @param dateText Date text to parse.
     * @return Parsed date.
     * @throws KoaraException If the text is not a valid date in yyyy-MM-dd format.
     */
    private static LocalDate parseDate(String dateText) throws KoaraException {
        assert dateText != null && !dateText.isBlank() : "Date text must not be blank";
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException exception) {
            throw new KoaraException(DATE_FORMAT_ERROR);
        }
    }
}

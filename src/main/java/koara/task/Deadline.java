package koara.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a particular date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates an incomplete deadline.
     *
     * @param description Description of the deadline.
     * @param by Date by which the task must be completed.
     */
    public Deadline(String description, LocalDate by) {
        super(TaskType.DEADLINE, description, "");
        this.by = by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(DISPLAY_DATE_FORMAT) + ")";
    }

    /**
     * Returns this deadline in the line-based format used by the data file.
     *
     * @return Serialized deadline data.
     */
    @Override
    public String toDataString() {
        return super.toDataString() + " | " + by;
    }
}

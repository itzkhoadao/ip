package koara;

/**
 * Represents a task that must be completed by a particular date or time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline.
     *
     * @param description Description of the deadline.
     * @param by Date or time by which the task must be completed.
     */
    public Deadline(String description, String by) {
        super(TaskType.DEADLINE, description, "");
        this.by = by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
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

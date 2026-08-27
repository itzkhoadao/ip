package koara;

/**
 * Represents a task that takes place between a start and end date or time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event.
     *
     * @param description Description of the event.
     * @param from Start date or time of the event.
     * @param to End date or time of the event.
     */
    public Event(String description, String from, String to) {
        super(TaskType.EVENT, description, "");
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns this event in the line-based format used by the data file.
     *
     * @return Serialized event data.
     */
    @Override
    public String toDataString() {
        return super.toDataString() + " | " + from + " | " + to;
    }
}

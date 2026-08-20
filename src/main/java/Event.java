// Represents a task that takes place between a start and end date or time.
public class Event extends Task {
    private final String from;
    private final String to;

    // Creates an incomplete event.
    public Event(String description, String from, String to) {
        super("E", description, "");
        this.from = from;
        this.to = to;
    }

    // Returns the event in string format using Level 4 display format.
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

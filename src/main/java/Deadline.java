// Represents a task that must be completed by a particular date or time.
public class Deadline extends Task {
    private final String by;

    // Creates an incomplete deadline.
    public Deadline(String description, String by) {
        super("D", description, "");
        this.by = by;
    }

    // Returns the deadline using the exact Level 4 display format.
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}

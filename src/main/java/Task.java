// This class represents a task with a description and completion status
public class Task {
    private final String typeIcon;
    private final String description;
    private final String additionalInformation;
    private boolean isDone;

    // Creates an incomplete task with its display type and optional timing information.
    public Task(String typeIcon, String description, String additionalInformation) {
        this.typeIcon = typeIcon;
        this.description = description;
        this.additionalInformation = additionalInformation;
        this.isDone = false;
    }

    // Mark task as done
    public void markAsDone() {
        this.isDone = true;
    }

    // Mark task as not done
    public void markAsNotDone() {
        this.isDone = false;
    }

    // Returns the icon used to display this task's completion status.
    public String getStatusIcon() {
        return this.isDone ? "X" : " ";
    }

    // Returns the string display form of this task (display according to Level 4's style)
    @Override
    public String toString() {
        return "[" + typeIcon + "][" + getStatusIcon() + "] " + description + additionalInformation;
    }
}

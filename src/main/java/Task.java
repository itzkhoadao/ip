
// This class represents a task with a description and completion status
public class Task {
    protected final String description;
    protected boolean isDone;

    // Creates a task that has not been completed.
    public Task(String description) {
        this.description = description;
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

    // Returns the string display form of this task.
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

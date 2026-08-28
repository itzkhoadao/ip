package koara.task;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final TaskType taskType;
    private final String description;
    private final String additionalInformation;
    private boolean isDone;

    /**
     * Creates an incomplete task with its display type and optional timing information.
     *
     * @param taskType Type of the task.
     * @param description Description of the task.
     * @param additionalInformation Additional information displayed after the description.
     */
    public Task(TaskType taskType, String description, String additionalInformation) {
        this.taskType = taskType;
        this.description = description;
        this.additionalInformation = additionalInformation;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the icon used to display this task's completion status.
     *
     * @return Completion status icon.
     */
    public String getStatusIcon() {
        return this.isDone ? "X" : " ";
    }

    /**
     * Returns whether this task's description contains the specified keyword.
     *
     * @param keyword Keyword to search for.
     * @return True if the description contains the keyword.
     */
    public boolean containsKeyword(String keyword) {
        return description.contains(keyword);
    }

    /**
     * Returns this task in the line-based format used by the data file.
     *
     * @return Serialized task data.
     */
    public String toDataString() {
        return taskType.getIcon() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + taskType.getIcon() + "][" + getStatusIcon() + "] " + description + additionalInformation;
    }
}

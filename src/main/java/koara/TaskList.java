package koara;

import java.util.ArrayList;

/**
 * Manages the tasks stored by Koara.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Creates a task list containing the specified tasks.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index Zero-based task index.
     * @return Task at the index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index Zero-based task index.
     * @return Removed task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index Zero-based task index.
     */
    public void mark(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index Zero-based task index.
     */
    public void unmark(int index) {
        tasks.get(index).markAsNotDone();
    }

    /**
     * Returns every task in the data-file format.
     *
     * @return Serialized task lines.
     */
    public ArrayList<String> toDataLines() {
        ArrayList<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            taskLines.add(task.toDataString());
        }
        return taskLines;
    }
}

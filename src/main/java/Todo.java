// Represents a task without any date or time information.
public class Todo extends Task {
    // Creates an incomplete task of type todo
    public Todo(String description) {
        super(TaskType.TODO, description, "");
    }
}

// Enum representing the supported task categories and their display icons.
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String icon;

    // associates each type with its display icon
    TaskType(String icon) {
        this.icon = icon;
    }

    // Returns the icon used to identify this task type.
    public String getIcon() {
        return icon;
    }
}

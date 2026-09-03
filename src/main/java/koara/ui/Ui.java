package koara.ui;

import java.util.Scanner;

/**
 * Handles interactions between Koara and the user.
 */
public class Ui implements AutoCloseable {
    private static final String DISPLAY_LINE_INDENT = "    ";
    private static final String DISPLAY_RESPONSE_INDENT = DISPLAY_LINE_INDENT + " ";
    private static final String DISPLAY_HORIZONTAL_LINE = DISPLAY_LINE_INDENT + "_".repeat(60);

    private final Scanner scanner;

    /**
     * Creates a UI that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays Koara's greeting.
     */
    public void showWelcome() {
        String banner = """
                 _  __  ___      _      ____       _
                | |/ / / _ \\    / \\    |  _ \\     / \\
                | ' / | | | |  / _ \\   | |_) |   / _ \\
                | . \\ | |_| | / ___ \\  |  _ <   / ___ \\
                |_|\\_\\ \\___/ /_/   \\_\\ |_| \\_\\ /_/   \\_\\
                """;
        showLine();
        System.out.print(banner.indent(DISPLAY_RESPONSE_INDENT.length()));
        showMessage("Hello! I'm Koara.");
        showMessage("What can I do for you?");
        showLine();
    }

    /**
     * Returns whether another command is available.
     *
     * @return True if another command can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return Next command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the horizontal response divider.
     */
    public void showLine() {
        System.out.println(DISPLAY_HORIZONTAL_LINE);
    }

    /**
     * Displays every line of a response using the standard indentation.
     *
     * @param response Response to display.
     */
    public void showResponse(String response) {
        response.lines().forEach(this::showMessage);
    }

    private void showMessage(String message) {
        System.out.println(DISPLAY_RESPONSE_INDENT + message);
    }

    /**
     * Closes the scanner used to read commands.
     */
    @Override
    public void close() {
        scanner.close();
    }
}

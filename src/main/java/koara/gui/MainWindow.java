package koara.gui;

import java.util.Objects;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import koara.Koara;

/**
 * Controls the main chatbot window defined in {@code MainWindow.fxml}.
 */
public class MainWindow extends AnchorPane {
    private static final Duration EXIT_DELAY = Duration.seconds(1);
    private static final String WELCOME_MESSAGE =
            "Hello! I'm Koara.\nWhat can I do for you?";

    private final Image userImage = loadImage("/images/DaUser.png");
    private final Image koaraImage = loadImage("/images/DaKoara.png");

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Koara koara;

    /**
     * Configures behavior that depends on the controls injected from FXML.
     */
    @FXML
    public void initialize() {
        dialogContainer.heightProperty().addListener(
                observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Supplies the Koara instance that processes commands.
     *
     * @param koara Koara instance used by this window.
     */
    public void setKoara(Koara koara) {
        this.koara = Objects.requireNonNull(koara);
        addKoaraDialog(WELCOME_MESSAGE);
        koara.getStartupError().ifPresent(this::addKoaraDialog);
    }

    /**
     * Processes the current input and displays the conversation.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = koara.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getKoaraDialog(response, koaraImage));
        userInput.clear();

        if (input.equals("bye")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition exitDelay = new PauseTransition(EXIT_DELAY);
            exitDelay.setOnFinished(event -> Platform.exit());
            exitDelay.play();
        }
    }

    /**
     * Adds a response from Koara without a preceding user message.
     *
     * @param response Response to display.
     */
    private void addKoaraDialog(String response) {
        dialogContainer.getChildren().add(
                DialogBox.getKoaraDialog(response, koaraImage));
    }

    /**
     * Loads an image required by the interface.
     *
     * @param resourcePath Classpath location of the image.
     * @return Loaded image.
     */
    private static Image loadImage(String resourcePath) {
        return new Image(Objects.requireNonNull(
                MainWindow.class.getResourceAsStream(resourcePath),
                "Missing image resource: " + resourcePath));
    }
}

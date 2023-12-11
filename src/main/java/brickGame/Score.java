package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * The {@code Score} class is responsible for displaying scores and messages in the game.
 */
public class Score {
    /**
     * Represents the score in the game.
     * This class serves as a placeholder for the player's score, and it does not contain additional methods or attributes.
     */
    public Score() {
    }


    /**
     * Displays a score label at the specified position.
     *
     * @param x     The X-coordinate for the label.
     * @param y     The Y-coordinate for the label.
     * @param score The score value to be displayed.
     * @param main  The main game instance.
     */
    public void show(final double x, final double y, int score, final Main main) {
        // Determine the sign of the score to display.
        String sign = (score >= 0) ? "+" : "";
        // Create a new label with the formatted score.
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);
        // Run the update on the JavaFX application thread.
        Platform.runLater(() -> main.root.getChildren().add(label));

        // Create a new thread to animate the score label.
        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    final int setI = i;
                    // Update label properties on the JavaFX application thread.
                    Platform.runLater(() -> {
                        label.setScaleX(setI);
                        label.setScaleY(setI);
                        label.setOpacity((20 - setI) / 20.0);
                    });
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Displays a message label at the center of the game screen.
     *
     * @param message The message to be displayed.
     * @param main    The main game instance.
     */
    public void showMessage(String message, final Main main) {
        // Create a new label with the specified message.
        final Label label = new Label(message);
        double xValue = (double) main.sceneWidth / 2;
        double yValue = (double) main.sceneHeight / 2;
        label.setTranslateX(xValue);
        label.setTranslateY(yValue);
        // Run the update on the JavaFX application thread.
        Platform.runLater(() -> main.root.getChildren().add(label));

        // Create a new thread to animate the message label.
        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    final int setI = i;
                    // Update label properties on the JavaFX application thread.
                    Platform.runLater(() -> {
                        label.setScaleX(Math.abs(setI - 10));
                        label.setScaleY(Math.abs(setI - 10));
                        label.setOpacity((20 - setI) / 20.0);
                    });
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
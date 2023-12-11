package brickGame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

/**
 * This class represents the pause menu in the game. It provides options for
 * resuming the game, returning to the main menu, displaying help, quitting the game,
 * and adjusting the volume.
 */
public class PauseMenu {
    /**
     * The PauseMenu class represents a menu that can be displayed to pause the game.
     * It provides options for pausing and managing game settings during gameplay.
     */
    public PauseMenu() {
    }


    private double volume;  // The current volume level
    private GameEngine engine = GameEngine.getInstance();  // The game engine instance

    /**
     * Pauses the game and displays the pause menu.
     *
     * @param main The Main class instance.
     * @param isInLevel A boolean indicating whether the game is currently in a level.
     */
    public void pauseGame(final Main main, boolean isInLevel) {
        if (isInLevel) {
            Sound sound = Sound.getInstance();  // The sound manager instance
            volume = sound.getVolume();  // Store the current volume

            // Create the pause menu layout
            VBox pauseRoot = new VBox(5);
            pauseRoot.setId("pauseMenu");

            Label paused = new Label("Paused");
            paused.setPadding(new Insets(0, 0, 60, 0));
            pauseRoot.setAlignment(Pos.CENTER);
            pauseRoot.setPadding(new Insets(25, 100, 120, 100));

            Button resume = new Button("Resume");
            Button main_menu = new Button("Return to Main Menu");
            Button quit = new Button("Quit Game");
            Button help = new Button("How to Play?");

            Label volumeLabel = new Label("Volume: " + volume);
            Slider volumeSlider = new Slider(0, 1, 0.5);
            volumeSlider.setShowTickLabels(true);
            volumeSlider.setShowTickMarks(true);
            volumeSlider.setMajorTickUnit(0.1);
            volumeSlider.setMinorTickCount(11);
            volumeSlider.setSnapToTicks(true);
            volumeSlider.setValue(volume);

            // Set up volume slider listeners
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double snapToTickValue = Math.round(newVal.doubleValue() / 0.1) * 0.1;
                volumeSlider.setValue(snapToTickValue);
            });

            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                volume = newValue.doubleValue();
                volumeLabel.setText(String.format("Volume: %.1f", volume));
            });

            // Add components to the pause menu
            pauseRoot.getChildren().addAll(paused, resume, main_menu, help, quit, volumeLabel, volumeSlider);
            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
            popupStage.initOwner(main.primaryStage);

            Scene scenePause = new Scene(pauseRoot, Color.TRANSPARENT);
            scenePause.getStylesheets().add("style.css");
            popupStage.setScene(scenePause);

            // Set up actions for buttons
            resume.setOnAction(event -> {
                sound.setVolume(volume);
                main.setVolume(volume);
                popupStage.hide();
                engine.start(main.time);
                sound.resumeBGM();
                sound.resumeVerstappen();
            });

            main_menu.setOnAction(event -> {
                popupStage.hide();
                sound.stopBGM();
                main.restartGame();
            });

            help.setOnAction(event -> {
                Help.showHelp(popupStage);
            });

            quit.setOnAction(event -> {
                main.quit();
            });

            // Stop the game engine and pause the background music
            engine.stop();
            sound.pauseBGM();
            sound.pauseVerstappen();
            popupStage.show();
        }
    }
}
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

public class PauseMenu {
    private double volume;
    private GameEngine engine = GameEngine.getInstance();

    public void pauseGame(final Main main, boolean isInLevel){
        if(isInLevel){
            Sound sound = Sound.getInstance();
            volume = sound.getVolume();

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

            Label volumeLabel = new Label("Volume: "+ volume);
            Slider volumeSlider = new Slider(0, 1, 0.5);
            volumeSlider.setShowTickLabels(true);
            volumeSlider.setShowTickMarks(true);
            volumeSlider.setMajorTickUnit(0.1);
            volumeSlider.setMinorTickCount(11);
            volumeSlider.setSnapToTicks(true);
            volumeSlider.setValue(volume);

            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double snapToTickValue = Math.round(newVal.doubleValue() / 0.1) * 0.1;
                volumeSlider.setValue(snapToTickValue);
            });

            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                volume = newValue.doubleValue();
                volumeLabel.setText(String.format("Volume: %.1f", volume));
            });

            pauseRoot.getChildren().addAll(paused, resume, main_menu, help, quit, volumeLabel, volumeSlider);
            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
            popupStage.initOwner(main.primaryStage);

            Scene scenePause = new Scene(pauseRoot, Color.TRANSPARENT);
            scenePause.getStylesheets().add("style.css");
            popupStage.setScene(scenePause);

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

            engine.stop();
            sound.pauseBGM();
            sound.pauseVerstappen();
            popupStage.show();
        }
    }
}

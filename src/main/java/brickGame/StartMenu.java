package brickGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class StartMenu {
    private int difficulty = 0;
    private final Button difficultyButton;

    public StartMenu(final Main main) {
        if (!main.primaryStage.isShowing()) {
            main.primaryStage.initStyle(StageStyle.UNDECORATED);
        }

        Button load = new Button("Load Game");
        Button newGame = new Button("Start New Game");
        difficultyButton = new Button("Difficulty: Easy");
        Button helpMenu = new Button("How To Play");
        Button quit = new Button("Quit Game");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(newGame, load, difficultyButton, helpMenu, quit);

        HBox hbox = new HBox();
        hbox.setId("rootMenu");
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().add(vbox);
        hbox.setPadding(new Insets(10));

        int sceneWidth = 540;
        int sceneHeight = 960;
        Scene scene = new Scene(hbox, sceneWidth, sceneHeight);

        difficultyButton.setOnAction(event -> {
            switch (difficulty) {
                case 0:
                    difficultyButton.setText("Difficulty: Medium");
                    difficulty = 2;
                    break;
                case 2:
                    difficultyButton.setText("Difficulty: Hard");
                    difficulty = 4;
                    break;
                case 4:
                    difficultyButton.setText("Difficulty: Easy");
                    difficulty = 0;
                    break;
            }
        });

        load.setOnAction(event -> {
            main.loadGame();
        });

        newGame.setOnAction(event -> {
            main.setDifficulty(difficulty);
            main.setNewStage(false);
        });

        helpMenu.setOnAction(event -> {
            Help.showHelp(main.primaryStage);
        });

        quit.setOnAction(event -> {
            main.primaryStage.close();
        });

        scene.getStylesheets().add("style.css");
        main.primaryStage.setResizable(false);
        main.primaryStage.sizeToScene();
        main.primaryStage.setScene(scene);
        main.primaryStage.centerOnScreen();
    }

    public void show(final Main main) {
        main.primaryStage.show();
    }
}

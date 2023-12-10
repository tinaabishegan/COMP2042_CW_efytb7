package brickGame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

/**
 * The StartMenu class represents the initial menu of the brick game.
 * It allows the user to start a new game, load an existing game,
 * adjust the game difficulty, access the help menu, and quit the game.
 */
public class StartMenu {
    private int difficulty = 0; // 0: Easy, 2: Medium, 4: Hard
    private final Button difficultyButton;

    /**
     * Constructs the StartMenu object.
     *
     * @param main The Main object controlling the game.
     */
    public StartMenu(final Main main) {
        if (!main.primaryStage.isShowing()) {
            main.primaryStage.initStyle(StageStyle.UNDECORATED);
        }

        // Buttons for various menu options
        Button load = new Button("Load Game");
        Button newGame = new Button("Start New Game");
        difficultyButton = new Button("Difficulty: Easy");
        Button helpMenu = new Button("How To Play");
        Button quit = new Button("Quit Game");

        // Create the vertical box for arranging buttons
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(newGame, load, difficultyButton, helpMenu, quit);

        // Create the horizontal box as the root of the scene
        HBox hbox = new HBox();
        hbox.setId("rootMenu");
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().add(vbox);
        hbox.setPadding(new Insets(10));

        // Set up the scene
        int sceneWidth = 540;
        int sceneHeight = 960;
        Scene scene = new Scene(hbox, sceneWidth, sceneHeight);

        // Handle difficulty button actions
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

        // Handle load button action
        load.setOnAction(event -> {
            main.loadGame();
        });

        // Handle new game button action
        newGame.setOnAction(event -> {
            main.setDifficulty(difficulty);
            main.setNewStage(false);
        });

        // Handle help menu button action
        helpMenu.setOnAction(event -> {
            Help.showHelp(main.primaryStage);
        });

        // Handle quit button action
        quit.setOnAction(event -> {
            main.primaryStage.close();
        });

        // Set up styles and display the stage
        scene.getStylesheets().add("style.css");
        main.primaryStage.setResizable(false);
        main.primaryStage.sizeToScene();
        main.primaryStage.setScene(scene);
        main.primaryStage.centerOnScreen();
    }

    /**
     * Displays the StartMenu.
     *
     * @param main The Main object controlling the game.
     */
    public void show(final Main main) {
        main.primaryStage.show();
    }
}

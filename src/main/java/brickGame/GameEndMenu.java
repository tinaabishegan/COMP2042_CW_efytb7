package brickGame;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import javafx.scene.layout.GridPane;

public class GameEndMenu {
    Sound sound = Sound.getInstance();

    class savedPlayer {
        String name;
        int score;
    }

    public void showGameEnd(final Main main, int level, int score) {
        Platform.runLater(() -> {
            String message;
            if (level< main.maxLevel) {
                message = "Game Over. Try again!\n You reached Level " + level + "\namassing " + score + " points!!";
            }
            else {
                message = "You Win!!!";
            }

            TextField userName = new TextField();
            userName.setPromptText("Enter your username");
            userName.getStyleClass().add("custom-text-field");
            userName.setPrefWidth(200);
            userName.setPrefHeight(50);
            userName.setTranslateX(615);
            userName.setTranslateY(200);

            Button submitButton = new Button("Submit");
            submitButton.setPrefWidth(200);
            submitButton.setPrefHeight(50);
            submitButton.setTranslateX(615);
            submitButton.setTranslateY(275);
            submitButton.setOnAction(e -> {
                saveScore(userName.getText(), score);
                submitButton.setDisable(true);
            });


            Label label = new Label(message);
            label.setScaleX(1.7);
            label.setScaleY(1.7);
            label.setTranslateX(600);
            label.setTranslateY(75);

            Button restart = new Button("Restart");
            restart.setPrefWidth(200);
            restart.setPrefHeight(50);
            restart.setTranslateX(615);
            restart.setTranslateY(425);
            restart.setOnAction(event -> {
                sound.stopGameOver();
                main.restartGame();
            });

            Button quit = new Button("Quit Game");
            quit.setPrefWidth(200);
            quit.setPrefHeight(50);
            quit.setTranslateX(615);
            quit.setTranslateY(500);
            quit.setOnAction(event -> main.quit());


            readScore(main, 125, 100);
            main.root.getChildren().addAll(label, userName, submitButton, restart, quit);
            main.root.setStyle("-fx-background-image: url('gameend.jpg');-fx-background-size: cover;");
        });
    }

    private void saveScore(String name, int score){
        try
        {
            String filename= "./save/leaderboard.txt";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            fw.write(name + "," + score +"\n");//appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public savedPlayer[] getTopPlayer(){
        int lines = 0;
        int i = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("./save/leaderboard.txt"))) {
            while (reader.readLine() != null) lines++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(lines);
        savedPlayer[] array = new savedPlayer[lines];
        try {
            File myObj = new File("./save/leaderboard.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitData = data.split(",");
                array[i] = new savedPlayer();
                array[i].name = splitData[0];
                array[i].score = Integer.parseInt(splitData[1]);
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Arrays.sort(array, Comparator.comparingInt((savedPlayer player) -> player.score).reversed());
        return array;
    }

    public void readScore(final Main main, int xPos, int yPos){
        Platform.runLater(() -> {
            savedPlayer[] array = getTopPlayer();

            Label titleLabel = new Label("LEADERBOARD");
            titleLabel.setScaleX(1.5);
            titleLabel.setScaleY(1.5);
            titleLabel.setTranslateX(25);

            GridPane gridPane = new GridPane();
            gridPane.setVgap(10);
            gridPane.setAlignment(Pos.TOP_CENTER);
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.add(titleLabel, 0, 0, 2, 1);

            for (int j = 0; j < Math.min(10, array.length); j++) {
                Label nameLabel = new Label(array[j].name);
                Label scoreLabel = new Label(Integer.toString(array[j].score));

                // add labels to the grid pane in separate columns
                gridPane.add(nameLabel, 0, j + 2);
                gridPane.add(scoreLabel, 1, j + 2);
            }

            gridPane.setStyle("-fx-border-color: #ffffff; -fx-border-width: 2; -fx-background-color: rgba(256, 256, 256, 0.3)");
            gridPane.setTranslateX(xPos);
            gridPane.setTranslateY(yPos);
            gridPane.setPrefSize(300, 600);

            main.root.getChildren().addAll(gridPane);
        });
    }
}
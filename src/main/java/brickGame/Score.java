package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

//import sun.plugin2.message.Message;

public class Score {

    public void show(final double x, final double y, int score, final Main main) {
        String sign = (score >= 0) ? "+" : "";
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> main.root.getChildren().add(label));

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    final int setI = i;
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

    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(440);
        label.setTranslateY(340);

        Platform.runLater(() -> main.root.getChildren().add(label));

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    final int setI = i;
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

    public void showGameEnd(final Main main, int level, int score) {
        Platform.runLater(() -> {
            String message = "default";
            if (level<18) {
                message = "Game Over. Try again!\n You reached Level " + level + "\namassing " + score + " points!!";
            }
            else {
                message = "You Win!!!";
            }
            Label label = new Label(message);
            label.setAlignment(Pos.CENTER);
            label.setTranslateX((main.sceneWidth - label.getWidth()) / 2);
            label.setTranslateY((main.sceneHeight - label.getHeight()) / 2);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Restart");
            restart.setTranslateX(main.sceneWidth/2+100);
            restart.setTranslateY(main.sceneHeight/2+100);
            restart.setOnAction(event -> main.restartGame());

            Button quit = new Button("Quit Game");
            quit.setTranslateX(main.sceneWidth/2+100);
            quit.setTranslateY(main.sceneHeight/2+300);
            quit.setOnAction(event -> main.quit());

            main.root.getChildren().addAll(label, restart, quit);
        });
    }
}


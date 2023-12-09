package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Label;

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
        double xValue = (double) main.sceneWidth /2;
        double yValue = (double) main.sceneHeight /2;
        label.setTranslateX(xValue);
        label.setTranslateY(yValue);
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
}
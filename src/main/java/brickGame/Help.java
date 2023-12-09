package brickGame;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Help {

    public static void showHelp(Stage stage) {
        Image helpBG = new Image("help.png");
        Popup popup = createPopup(helpBG, stage);

        popup.show(stage, 710, 140);
    }

    private static Popup createPopup(Image helpImage, Stage stage) {
        Popup popup = new Popup();

        ImageView helpBGView = new ImageView(helpImage);
        helpBGView.setFitWidth(500);
        helpBGView.setFitHeight(629);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popup.hide());

        VBox popupLayout = new VBox(10);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.getChildren().addAll(helpBGView, closeButton);

        popup.getContent().add(popupLayout);

        return popup;
    }
}

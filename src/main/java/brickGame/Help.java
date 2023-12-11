package brickGame;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * The Help class provides a method to display a help popup with an image and a close button.
 */
public class Help {
    /**
     * Default constructor for the Help class.
     * Constructs an instance of the Help class with no parameters.
     */
    public Help() {
    }


    /**
     * Displays the help popup with the specified background image.
     *
     * @param stage The main stage of the application.
     */
    public static void showHelp(Stage stage) {
        // Load the help background image
        Image helpBG = new Image("help.png");

        // Create and display the popup
        Popup popup = createPopup(helpBG, stage);
        popup.show(stage, 710, 140);
    }

    /**
     * Creates a popup with the specified help image and a close button.
     *
     * @param helpImage The background image for the help popup.
     * @param stage     The main stage of the application.
     * @return The created popup.
     */
    private static Popup createPopup(Image helpImage, Stage stage) {
        // Create a new popup
        Popup popup = new Popup();

        // Create an ImageView to display the help background image
        ImageView helpBGView = new ImageView(helpImage);
        helpBGView.setFitWidth(500);
        helpBGView.setFitHeight(629);

        // Create a close button and set its action to hide the popup
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popup.hide());

        // Create a VBox to layout the help image and the close button
        VBox popupLayout = new VBox(10);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.getChildren().addAll(helpBGView, closeButton);

        // Add the layout to the popup's content
        popup.getContent().add(popupLayout);

        return popup;
    }
}

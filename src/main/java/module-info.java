/**
 * The brickGame module contains classes and resources for a basic brick-breaking game using JavaFX.
 * It requires JavaFX modules for FXML, controls, and media.
 *
 * To use this module, ensure that the required JavaFX dependencies are available in the classpath.
 *
 * The module opens the 'brickGame' package to JavaFX FXML and exports the 'brickGame' package.
 *
 */
module brickGame {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;

    opens brickGame to javafx.fxml;
    exports brickGame;
}

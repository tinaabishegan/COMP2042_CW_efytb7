package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;

/**
 * Represents a bonus object in the brick game.
 * Bonuses provide additional features or points when collected by the player.
 */
public class Bonus implements Serializable {

    /** The graphical representation of the bonus using a JavaFX Rectangle. */
    public Rectangle bonus;

    /** The x-coordinate of the bonus on the game board. */
    public double x;

    /** The y-coordinate of the bonus on the game board. */
    public double y;

    /** The time at which the bonus was created (in milliseconds). */
    public long timeCreated;

    /** Indicates whether the bonus has been taken by the player. */
    public boolean taken = false;

    /**
     * Constructs a Bonus object at the specified row and column position.
     *
     * @param row    The row index of the bonus on the game board.
     * @param column The column index of the bonus on the game board.
     */
    public Bonus(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;
        draw();
    }

    /**
     * Initializes the graphical representation of the bonus using a JavaFX Rectangle
     * and sets its dimensions, position, and image.
     */
    private void draw() {
        bonus = new Rectangle();
        bonus.setWidth(30);
        bonus.setHeight(30);
        bonus.setX(x);
        bonus.setY(y);
        // Assuming the image file is named "bonus1.png"
        String url = "bonus1.png";
        bonus.setFill(new ImagePattern(new Image(url)));
    }
}

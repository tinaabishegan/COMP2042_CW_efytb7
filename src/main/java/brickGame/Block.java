package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;

/**
 * The Block class represents a block in the brick game.
 * Each block has a position, color, type, and can be destroyed.
 */
public class Block implements Serializable {

    /**
     * A static block instance used for retrieving common properties.
     */
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);

    // Block properties
    /** The row position of the block. */
    public int row;

    /** The column position of the block. */
    public int column;

    /** Indicates whether the block is destroyed. */
    public boolean isDestroyed = false;

    /** The color of the block. */
    private Color color;

    /** The type of the block. */
    public int type;

    /** The x-coordinate of the block. */
    public int x;

    /** The y-coordinate of the block. */
    public int y;

    /** The width of the block. */
    private final int width = 100;

    /** The height of the block. */
    private final int height = 30;

    /** The padding at the top of the block. */
    private final int paddingTop = height * 2;

    /** The horizontal padding of the block. */
    private final int paddingH = 70;

    /** The rectangle representing the block. */
    public Rectangle rect;

    // Constants for block hit detection

    /** Constant representing no hit. */
    public static int NO_HIT = -1;

    /** Constant representing a hit on the right side of the block. */
    public static int HIT_RIGHT = 0;

    /** Constant representing a hit at the bottom of the block. */
    public static int HIT_BOTTOM = 1;

    /** Constant representing a hit on the left side of the block. */
    public static int HIT_LEFT = 2;

    /** Constant representing a hit at the top of the block. */
    public static int HIT_TOP = 3;

    /** Constant representing a hit at the bottom-right corner of the block. */
    public static int HIT_BOTTOM_RIGHT = 4;

    /** Constant representing a hit at the bottom-left corner of the block. */
    public static int HIT_BOTTOM_LEFT = 5;

    /** Constant representing a hit at the top-right corner of the block. */
    public static int HIT_TOP_RIGHT = 6;

    /** Constant representing a hit at the top-left corner of the block. */
    public static int HIT_TOP_LEFT = 7;

    /** Constant representing a normal block. */
    public static int BLOCK_NORMAL = 99;

    /** Constant representing a bonus block. */
    public static int BLOCK_BONUS = 100;

    /** Constant representing a star block. */
    public static int BLOCK_STAR = 101;

    /** Constant representing a heart block. */
    public static int BLOCK_HEART = 102;

    /** Constant representing a special Verstappen block. */
    public static int BLOCK_VERSTAPPEN = 103;

    /**
     * Constructs a Block with specified parameters.
     *
     * @param row    The row index of the block.
     * @param column The column index of the block.
     * @param color  The color of the block.
     * @param type   The type of the block.
     */
    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;
        draw();
    }

    /**
     * Initializes the graphical representation of the block.
     */
    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;
        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        // Set block fill based on type
        if (type == BLOCK_BONUS) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_VERSTAPPEN) {
            Image image = new Image("verstappen.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }
    }

    /**
     * Checks if the block is hit by a ball and returns the type of hit.
     *
     * @param xBall      The x-coordinate of the ball.
     * @param yBall      The y-coordinate of the ball.
     * @param ballRadius The radius of the ball.
     * @return The type of hit (or no hit).
     */
    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) {
            return NO_HIT;
        }

        // Check collision with the bottom of the block
        if (xBall >= x && xBall <= x + width && yBall - ballRadius <= y + height && yBall + ballRadius > y + height) {
            // Check collision with the right side of the block, effectively checking bottom-right corner collision
            if (yBall >= y && yBall <= y + height && xBall - ballRadius <= x + width && xBall + ballRadius > x + width){
                return HIT_BOTTOM_RIGHT;
            }
            // Check collision with the left side of the block, effectively checking bottom-left corner collision
            else if (yBall >= y && yBall <= y + height && xBall + ballRadius >= x && xBall - ballRadius < x){
                return HIT_BOTTOM_LEFT;
            }
            //Otherwise it is just the bottom side that was hit
            return HIT_BOTTOM;
        }

        // Check collision with the top of the block
        if (xBall >= x && xBall <= x + width && yBall + ballRadius >= y && yBall - ballRadius < y) {
            // Check collision with the right side of the block, effectively checking top-right corner collision
            if (yBall >= y && yBall <= y + height && xBall - ballRadius <= x + width && xBall + ballRadius > x + width){
                return HIT_TOP_RIGHT;
            }
            // Check collision with the left side of the block, effectively checking top-left corner collision
            else if (yBall >= y && yBall <= y + height && xBall + ballRadius >= x && xBall - ballRadius < x){
                return HIT_TOP_LEFT;
            }
            //Otherwise it is just the top side that was hit
            return HIT_TOP;
        }

        // Check collision with the right side of the block
        if (yBall >= y && yBall <= y + height && xBall - ballRadius <= x + width && xBall + ballRadius > x + width) {
            return HIT_RIGHT;
        }

        // Check collision with the left side of the block
        if (yBall >= y && yBall <= y + height && xBall + ballRadius >= x && xBall - ballRadius < x) {
            return HIT_LEFT;
        }

        return NO_HIT;
    }

    /**
     * Gets the padding at the top of the block area.
     *
     * @return The top padding.
     */
    public static int getPaddingTop() {
        return block.paddingTop;
    }

    /**
     * Gets the horizontal padding of the block area.
     *
     * @return The horizontal padding.
     */
    public static int getPaddingH() {
        return block.paddingH;
    }

    /**
     * Gets the height of the blocks.
     *
     * @return The height of the blocks.
     */
    public static int getHeight() {
        return block.height;
    }

    /**
     * Gets the width of the blocks.
     *
     * @return The width of the blocks.
     */
    public static int getWidth() {
        return block.width;
    }
}

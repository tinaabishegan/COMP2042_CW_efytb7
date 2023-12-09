package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;

public class Block implements Serializable {
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);
    public int row;
    public int column;
    public boolean isDestroyed = false;
    private Color color;
    public int type;
    public int x;
    public int y;
    private int width = 100;
    private int height = 30;
    private int paddingTop = height * 2;
    private int paddingH = 70;
    public Rectangle rect;
    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;
    public static int HIT_BOTTOM_RIGHT = 4;
    public static int HIT_BOTTOM_LEFT = 5;
    public static int HIT_TOP_RIGHT = 6;
    public static int HIT_TOP_LEFT = 7;
    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_BONUS = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static int BLOCK_VERSTAPPEN = 103;

    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;
        draw();
    }

    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;
        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

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


    // Added ballRadius parameter to take ballRadius into consideration so that the collisions to blocks are more accurate and the ball doesn't phase through the blocks.
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

    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }

}

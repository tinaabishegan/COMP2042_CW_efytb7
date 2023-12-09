package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Bonus implements Serializable {
    public Rectangle bonus;

    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;

    public Bonus(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;

        draw();
    }

    private void draw() {
        bonus = new Rectangle();
        bonus.setWidth(30);
        bonus.setHeight(30);
        bonus.setX(x);
        bonus.setY(y);

        String url = "bonus1.png";

        bonus.setFill(new ImagePattern(new Image(url)));
    }



}

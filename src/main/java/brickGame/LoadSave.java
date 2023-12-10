package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * The LoadSave class represents the functionality to read saved game data from a file.
 */
public class LoadSave {
    /**
     * Default constructor for the LoadSave class.
     * Constructs an instance of the class with no parameters.
     */
    public LoadSave() {
    }


    // Game state variables
    /**
     * Represents whether a heart block exists in the game.
     */
    public boolean isExistHeartBlock;

    /**
     * Represents whether the player has a gold status in the game.
     */
    public boolean isGoldStatus;

    /**
     * Represents whether the ball should move downward.
     */
    public boolean goDownBall;

    /**
     * Represents whether the ball should move to the right.
     */
    public boolean goRightBall;

    /**
     * Represents whether the ball should collide to break a block.
     */
    public boolean collideToBreak;

    /**
     * Represents whether the ball should collide to break a block and move to the right.
     */
    public boolean collideToBreakAndMoveToRight;

    /**
     * Represents whether the ball has collided with the right wall.
     */
    public boolean collideToRightWall;

    /**
     * Represents whether the ball has collided with the left wall.
     */
    public boolean collideToLeftWall;

    /**
     * Represents whether the ball has collided with a block on the right.
     */
    public boolean collideToRightBlock;

    /**
     * Represents whether the ball has collided with a block on the bottom.
     */
    public boolean collideToBottomBlock;

    /**
     * Represents whether the ball has collided with a block on the left.
     */
    public boolean collideToLeftBlock;

    /**
     * Represents whether the ball has collided with a block on the top.
     */
    public boolean collideToTopBlock;

    /**
     * Represents the difficulty level of the game.
     */
    public int difficulty;

    /**
     * Represents the current level of the game.
     */
    public int level;

    /**
     * Represents the player's current score in the game.
     */
    public int score;

    /**
     * Represents the number of hearts the player has.
     */
    public int heart;

    /**
     * Represents the count of blocks destroyed by the player.
     */
    public int destroyedBlockCount;

    /**
     * The x-coordinate of the ball in the game.
     */
    public double xBall;

    /**
     * The y-coordinate of the ball in the game.
     */
    public double yBall;

    /**
     * The x-coordinate of the break in the game.
     */
    public double xBreak;

    /**
     * The y-coordinate of the break in the game.
     */
    public double yBreak;

    /**
     * The center x-coordinate of the break in the game.
     */
    public double centerBreakX;

    /**
     * The current time elapsed in the game.
     */
    public long time;

    /**
     * The time at which the player achieved gold status in the game.
     */
    public long goldTime;

    /**
     * The velocity of the ball in the x-direction.
     */
    public double vX;

    /**
     * The velocity of the ball in the y-direction.
     */
    public double vY;

    /**
     * An ArrayList containing serializable Block objects representing blocks in the game.
     */
    public ArrayList<BlockSerializable> blocks = new ArrayList<BlockSerializable>();


    /**
     * Reads saved game data from a file.
     */
    public void read() {
        try {
            // Create an ObjectInputStream to read from the save file
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(Main.savePath)));

            // Read basic game state information
            difficulty = inputStream.readInt();
            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = 0;
            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xBreak = inputStream.readDouble();
            yBreak = inputStream.readDouble();
            centerBreakX = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();
            vY = inputStream.readDouble();
            isExistHeartBlock = inputStream.readBoolean();
            isGoldStatus = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            collideToBreak = inputStream.readBoolean();
            collideToBreakAndMoveToRight = inputStream.readBoolean();
            collideToRightWall = inputStream.readBoolean();
            collideToLeftWall = inputStream.readBoolean();
            collideToRightBlock = inputStream.readBoolean();
            collideToBottomBlock = inputStream.readBoolean();
            collideToLeftBlock = inputStream.readBoolean();
            collideToTopBlock = inputStream.readBoolean();

            try {
                // Read the ArrayList of BlockSerializable objects
                blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

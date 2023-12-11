package brickGame;

import java.io.Serializable;

/**
 * The {@code BlockSerializable} class represents a serializable block in a brick game.
 * Each block has a specified row, column, and type.
 */
public class BlockSerializable implements Serializable {

    /**
     * The row index of the block.
     */
    public final int row;

    /**
     * The column index of the block.
     */
    public final int j;

    /**
     * The type of the block.
     */
    public final int type;

    /**
     * Constructs a new {@code BlockSerializable} instance with the specified parameters.
     *
     * @param row  The row index of the block.
     * @param j    The column index of the block.
     * @param type The type of the block.
     */
    public BlockSerializable(int row, int j, int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}

package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.io.*;
import java.util.*;

/**
 * The main class for the Brick Breaker game.
 * This class handles the game logic, user input, and UI elements.
 * It also includes methods for saving and loading the game, managing game state, handling collisions,
 * and controlling the game loop through onUpdate, onPhysicsUpdate, and onTime methods.
 */
public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    /**
     * Constants for direction.
     */
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    /**
     * Object used for synchronization.
     */
    private final Object lock = new Object();

    /**
     * Current level of the game.
     */
    private int level = 0;

    /**
     * X-coordinate for the break object.
     */
    private double xBreak = 205.0f;

    /**
     * Center X-coordinate for the break object.
     */
    private double centerBreakX;

    /**
     * Y-coordinate for the break object.
     */
    private double yBreak = 930.0f;

    /**
     * X-coordinate for the Verstappen object.
     */
    private double xVerstappen = 0;

    /**
     * Y-coordinate for the Verstappen object.
     */
    private double yVerstappen = 0;

    /**
     * Width of the break object.
     */
    private int breakWidth = 130;

    /**
     * Height of the break object.
     */
    private int breakHeight = 30;

    /**
     * Half of the break width.
     */
    private int halfBreakWidth = breakWidth / 2;

    /**
     * Width of the scene.
     */
    public int sceneWidth = 540;

    /**
     * Height of the scene.
     */
    public int sceneHeight = 960;

    /**
     * The ball object.
     */
    private Circle ball;

    /**
     * X-coordinate of the ball.
     */
    private double xBall;

    /**
     * Y-coordinate of the ball.
     */
    private double yBall;

    /**
     * Volume level of the game.
     */
    private double volume = 0.5;

    /**
     * Flag indicating whether the game is in a level.
     */
    private boolean isInLevel = false;

    /**
     * Flag indicating whether the maximum time is reached.
     */
    private boolean isMaxTime = false;

    /**
     * Flag indicating whether the gold status is achieved.
     */
    private boolean isGoldStatus = false;

    /**
     * Flag indicating whether a heart block exists.
     */
    private boolean isExistHeartBlock = false;

    /**
     * Flag indicating whether a Verstappen block exists.
     */
    private boolean isExistVerstappenBlock = false;

    /**
     * Rectangle object.
     */
    private Rectangle rect;

    /**
     * Verstappen object.
     */
    private Rectangle verstappen;

    /**
     * Radius of the ball.
     */
    private final int ballRadius = 10;

    /**
     * Count of destroyed blocks.
     */
    private int destroyedBlockCount = 0;

    /**
     * Difficulty level of the game.
     */
    private int difficulty = 0;

    /**
     * Number of hearts available.
     */
    private int heart = 3;

    /**
     * Current score of the player.
     */
    private int score = 0;

    /**
     * Time elapsed in the game.
     */
    public long time = 0;

    /**
     * Time to achieve gold status.
     */
    private long goldTime = 0;

    /**
     * Flag indicating the ball should go down.
     */
    private boolean goDownBall = false;

    /**
     * Flag indicating the ball should go right.
     */
    private boolean goRightBall = true;

    /**
     * Flag indicating collision with the break.
     */
    private boolean collideToBreak = false;

    /**
     * Flag indicating collision with the break and moving to the right.
     */
    private boolean collideToBreakAndMoveToRight = true;

    /**
     * Flag indicating collision with the right wall.
     */
    private boolean collideToRightWall = false;

    /**
     * Flag indicating collision with the left wall.
     */
    private boolean collideToLeftWall = false;

    /**
     * Flag indicating collision with the right block.
     */
    private boolean collideToRightBlock = false;

    /**
     * Flag indicating collision with the bottom block.
     */
    private boolean collideToBottomBlock = false;

    /**
     * Flag indicating collision with the left block.
     */
    private boolean collideToLeftBlock = false;

    /**
     * Flag indicating collision with the top block.
     */
    private boolean collideToTopBlock = false;

    /**
     * Game engine instance.
     */
    private GameEngine engine = GameEngine.getInstance();

    /**
     * Path to save game data.
     */
    public static String savePath = "./save/save.mdds";

    /**
     * Directory path for saving game data.
     */
    public static String savePathDir = "./save/";

    /**
     * List of block objects in the game.
     */
    private ArrayList<Block> blocks = new ArrayList<Block>();

    /**
     * List of bonus objects in the game.
     */
    private ArrayList<Bonus> bonuses = new ArrayList<Bonus>();

    /**
     * Array of colors for blocks.
     */
    private final Color[] colors = new Color[]{
            Color.rgb(173, 216, 230), // Pastel Blue
            Color.rgb(144, 238, 144), // Pastel Green
            Color.rgb(255, 255, 153), // Pastel Yellow
            Color.rgb(230, 230, 250), // Pastel Lavender
            Color.rgb(255, 218, 185), // Pastel Peach
            Color.rgb(200, 162, 200)  // Pastel Lilac
    };

    /**
     * Root pane for the game.
     */
    public Pane root;

    /**
     * Label displaying the current score.
     */
    private Label scoreLabel;

    /**
     * Label displaying the number of hearts.
     */
    private Label heartLabel;

    /**
     * Flag indicating whether to load the game from a saved state.
     */
    private boolean loadFromSave = false;

    /**
     * Velocity in the X-direction for the ball.
     */
    private double vX = 1.000;

    /**
     * Velocity in the Y-direction for the ball.
     */
    private double vY = 1.000;

    /**
     * Default velocity in the X-direction for the ball.
     */
    private double defaultvX = 1.000;

    /**
     * Default velocity in the Y-direction for the ball.
     */
    private double defaultvY = 1.000;

    /**
     * Speed of the break object.
     */
    private double breakSpeed = 4.000;

    /**
     * Maximum level in the game.
     */
    public int maxLevel;

    /**
     * Reference to the primary stage of the game.
     */
    public Stage primaryStage = null;

    /**
     * Instance of the Sound class for managing game sounds.
     */
    Sound sound = Sound.getInstance();

    /**
     * Background image for the game.
     */
    Image backgroundImage;

    /**
     * Default constructor for the Main class.
     * This constructor initializes an instance of the Main class.
     */
    public Main() {
    }


    /**
     * The main entry point for the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application by showing the start menu.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        StartMenu startMenu = new StartMenu(this);
        startMenu.show(this);
    }

    /**
     * Sets the difficulty level of the game.
     *
     * @param difficulty The difficulty level to set.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        maxLevel = 8 * (difficulty + 1) + 1;
        breakSpeed = 5 - difficulty;
        defaultvX = 1 + difficulty;
        defaultvY = 1 + difficulty;
        sceneWidth = 540 + difficulty * Block.getWidth();
        backgroundImage = new Image("/bg" + difficulty + ".jpg");
    }

    /**
     * Starts a new game level.
     *
     * @param primaryStage The primary stage for the application.
     * @throws Exception if an error occurs during level initialization.
     */
    public void startLevel(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        root = new Pane();

        if (!loadFromSave) {
            level++;
            if (level > 1) {
                new Score().showMessage("Level Up :)", this);
            }
            if (level == maxLevel) {
                gameEnd();
                return;
            }
            initBall();
            initBreak();
            initBoard();
        } else {
            if (isGoldStatus) {
                ball.setFill(new ImagePattern(new Image("goldball.png")));
            }
            loadFromSave = false;
        }

        engine = GameEngine.getInstance();
        engine.setOnAction(this);
        engine.setFps(120);
        if (!loadFromSave) {
            engine.start();
        } else {
            engine.start(time);
        }
        setStage(root);
    }

    /**
     * Sets up the game stage with UI elements.
     *
     * @param root The root pane of the scene.
     */
    public void setStage(Pane root) {
        scoreLabel = new Label("Score: " + score);
        Label levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 120);
        root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }
        setScene(root);
    }

    /**
     * Ends the game and shows the game over screen.
     */
    public void gameEnd() {
        sound.playGameOver("outro.mp3");
        sceneWidth = 960;
        isInLevel = false;
        sound.stopBGM();
        root = new Pane();
        setScene(root);
        new GameEndMenu().showGameEnd(this, level, score);
    }

    /**
     * Sets the scene for the game with background image and event handling.
     *
     * @param root The root pane of the scene.
     */
    private void setScene(Pane root) {
        root.setStyle("-fx-background-image: url('" + backgroundImage.getUrl() + "');-fx-background-size: cover;");
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    /**
     * Initializes the ball at a random position.
     */
    private void initBall() {
        Random random = new Random();
        xBall = random.nextInt(sceneWidth) + 1;
        yBall = random.nextInt(sceneHeight - 150 - ((level / (difficulty + 1) + 1) * Block.getHeight())) + ((level / (difficulty + 1) + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }

    /**
     * Initializes the breaking paddle.
     */
    private void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);
        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
        rect.setFill(pattern);
    }

    /**
     * Initializes the game board with blocks.
     */
    private void initBoard() {
        for (int i = 0; i < 4 + difficulty; i++) {
            for (int j = 0; j < level / (difficulty + 1) + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_BONUS;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else if (r % 10 == 4) {
                    if (!isExistVerstappenBlock) {
                        type = Block.BLOCK_VERSTAPPEN;
                        isExistVerstappenBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
            }
        }
    }

    /**
     * Handles user input events.
     *
     * @param event The KeyEvent representing the user input.
     */
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:
                move(RIGHT);
                break;
            case S:
                saveGame();
                break;
            case P:
                PauseMenu pauseMenu = new PauseMenu();
                pauseMenu.pauseGame(this, isInLevel);
                break;
        }
    }

    /**
     * Moves the breaking paddle in the specified direction.
     *
     * @param direction The direction of the movement (LEFT or RIGHT).
     */
    private void move(final int direction) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(30); // Run for 30 cycles
        KeyFrame moveFrame = new KeyFrame(Duration.millis(breakSpeed), event -> {
            if ((xBreak == (sceneWidth - breakWidth) && direction == RIGHT) || (xBreak == 0 && direction == LEFT)) {
                timeline.stop();
            } else {
                if (direction == RIGHT) {
                    xBreak++;
                } else {
                    xBreak--;
                }
                centerBreakX = xBreak + halfBreakWidth;
            }
        });
        timeline.getKeyFrames().add(moveFrame);
        timeline.play();
    }

    /**
     * Resets the collision flags to false.
     */
    private void resetCollideFlags() {
        collideToBreak = false;
        collideToBreakAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;
        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

    /**
     * Sets the physics for the ball's movement.
     */
    private void setPhysicsToBall() {
        if (goDownBall) {
            yBall += vY;
        } else {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        } else {
            xBall -= vX;
        }

        if (xBall - ballRadius <= 0) {
            resetCollideFlags();
            collideToLeftWall = true;
        }

        if (xBall + ballRadius >= sceneWidth) {
            resetCollideFlags();
            collideToRightWall = true;
        }

        if (yBall - ballRadius <= 0) {
            resetCollideFlags();
            goDownBall = true;
            return;
        }

        if (yBall + ballRadius >= sceneHeight) {
            resetCollideFlags();
            goDownBall = false;
            if (!isGoldStatus) {
                heart--;
                new Score().show(sceneWidth / 2, sceneHeight / 2, -1, this);
                if (heart == 0) {
                    Platform.runLater(() -> {
                        gameEnd();
                    });
                    nextLevel();
                }
            }
            return;
        }

        if (yBall >= yBreak - ballRadius && yBall <= yBreak + breakHeight + ballRadius){
            if (xBall >= xBreak - ballRadius && xBall <= xBreak + breakWidth + ballRadius) {
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;
                double relation = (xBall - centerBreakX) / (breakWidth / 2);

                if (Math.abs(relation) <= 0.3) {
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level/(difficulty+1) / 3.500);
                } else {
                    vX = (Math.abs(relation) * 2) + (level/(difficulty+1) / 3.500);
                }
                collideToBreakAndMoveToRight = xBall - centerBreakX > 0;
            }
        }

        if (collideToBreak) {
            goRightBall = collideToBreakAndMoveToRight;
        }

        //Wall collide
        if (collideToRightWall) {
            goRightBall = false;
        }

        if (collideToLeftWall) {
            goRightBall = true;
        }

        //Block collide
        if (collideToRightBlock) {
            goRightBall = true;
        }

        if (collideToLeftBlock) {
            goRightBall = true;
        }

        if (collideToTopBlock) {
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            goDownBall = true;
        }
    }

    /**
     * Saves the current game state to a file in a separate thread.
     */
    private void saveGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new File(savePathDir).mkdirs();
                File file = new File(savePath);
                ObjectOutputStream outputStream = null;
                try {
                    outputStream = new ObjectOutputStream(new FileOutputStream(file));
                    outputStream.writeInt(difficulty);
                    outputStream.writeInt(level);
                    outputStream.writeInt(score);
                    outputStream.writeInt(heart);
                    outputStream.writeDouble(xBall);
                    outputStream.writeDouble(yBall);
                    outputStream.writeDouble(xBreak);
                    outputStream.writeDouble(yBreak);
                    outputStream.writeDouble(centerBreakX);
                    outputStream.writeLong(time);
                    outputStream.writeLong(goldTime);
                    outputStream.writeDouble(vX);
                    outputStream.writeDouble(vY);
                    outputStream.writeBoolean(isExistHeartBlock);
                    outputStream.writeBoolean(isGoldStatus);
                    outputStream.writeBoolean(goDownBall);
                    outputStream.writeBoolean(goRightBall);
                    outputStream.writeBoolean(collideToBreak);
                    outputStream.writeBoolean(collideToBreakAndMoveToRight);
                    outputStream.writeBoolean(collideToRightWall);
                    outputStream.writeBoolean(collideToLeftWall);
                    outputStream.writeBoolean(collideToRightBlock);
                    outputStream.writeBoolean(collideToBottomBlock);
                    outputStream.writeBoolean(collideToLeftBlock);
                    outputStream.writeBoolean(collideToTopBlock);

                    ArrayList<BlockSerializable> blockSerializables = new ArrayList<BlockSerializable>();
                    for (Block block : blocks) {
                        if (block.isDestroyed) {
                            continue;
                        }
                        blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                    }

                    outputStream.writeObject(blockSerializables);

                    new Score().showMessage("Game Saved", Main.this);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * Loads the game state from a file.
     */
    public void loadGame() {
        LoadSave loadSave = new LoadSave();
        loadSave.read();
        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStatus = loadSave.isGoldStatus;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        collideToBreak = loadSave.collideToBreak;
        collideToBreakAndMoveToRight = loadSave.collideToBreakAndMoveToRight;
        collideToRightWall = loadSave.collideToRightWall;
        collideToLeftWall = loadSave.collideToLeftWall;
        collideToRightBlock = loadSave.collideToRightBlock;
        collideToBottomBlock = loadSave.collideToBottomBlock;
        collideToLeftBlock = loadSave.collideToLeftBlock;
        collideToTopBlock = loadSave.collideToTopBlock;
        difficulty = loadSave.difficulty;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;
        vY = loadSave.vY;
        blocks.clear();
        bonuses.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }
        setNewStage(true);
    }

    /**
     * Sets up a new game stage, including initializing ball, break, and bonuses.
     *
     * @param loadFromSave True if loading from a saved game, false for a new game.
     */
    public void setNewStage(boolean loadFromSave){
        sound.playBGM("bgm.mp3");
        initBall();
        initBreak();
        if(loadFromSave) {
            setDifficulty(difficulty);
        }
        try {
            vX = defaultvX;
            vY = defaultvY;
            this.loadFromSave = loadFromSave;
            isInLevel=true;
            startLevel(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the current game stage, including velocity, collisions, and block configurations.
     */
    private void resetStage() {
        synchronized (lock){
            if(isMaxTime){
                isMaxTime=false;
                removeMax();
            }
        }
        vX = defaultvX;
        vY = defaultvY;
        xBreak=205;
        resetCollideFlags();
        goDownBall = false;
        isGoldStatus = false;
        isExistHeartBlock = false;
        isExistVerstappenBlock=false;
        time = 0;
        goldTime = 0;
        blocks.clear();
        bonuses.clear();
        destroyedBlockCount = 0;
    }

    /**
     * Checks if the required number of blocks are destroyed to move to the next level.
     */
    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            nextLevel();
        }
    }

    /**
     * Moves the game to the next level by resetting the stage.
     */
    private void nextLevel() {
        synchronized (lock){
            Platform.runLater(() -> {
                try {
                    resetStage();
                    engine.stop();
                    if(heart!=0) {
                        startLevel(primaryStage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Restarts the game with default settings.
     */
    public void restartGame() {
        try {
            isInLevel=false;
            difficulty=0;
            sceneWidth=540;
            level = 0;
            heart = 3;
            score = 0;
            resetStage();
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the UI elements during the game loop.
     */
    @Override
    public void onUpdate() {
        synchronized (lock){
            Platform.runLater(() -> {
                scoreLabel.setText("Score: " + score);
                heartLabel.setText("Heart : " + heart);

                rect.setX(xBreak);
                rect.setY(yBreak);
                ball.setCenterX(xBall);
                ball.setCenterY(yBall);

                for (Bonus choco : bonuses) {
                    choco.bonus.setY(choco.y);
                }
            });

            if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop() || isMaxTime) {
                for (final Block block : blocks) {
                    int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                    int maxCode = block.checkHitToBlock(xVerstappen+breakWidth, yVerstappen+15,ballRadius);
                    if (hitCode != Block.NO_HIT || maxCode != Block.NO_HIT) {
                        sound.playSFX("hit.mp3");
                        score += 1;

                        new Score().show(block.x, block.y, 1, this);

                        Platform.runLater(() -> block.rect.setVisible(false));
                        block.isDestroyed = true;
                        destroyedBlockCount++;
                        //System.out.println("size is " + blocks.size());
                        resetCollideFlags();

                        if (block.type == Block.BLOCK_BONUS) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = time;
                            Platform.runLater(() -> root.getChildren().add(choco.bonus));
                            bonuses.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) {
                            goldTime = time;
                            Platform.runLater(() -> ball.setFill(new ImagePattern(new Image("goldball.png"))));
                            new Score().showMessage("GOLDEN", this);
                            isGoldStatus = true;
                        }

                        if (block.type == Block.BLOCK_HEART) {
                            heart++;
                        }

                        if (block.type == Block.BLOCK_VERSTAPPEN) {
                            spawnMax(block.row, difficulty);
                        }

                        if (hitCode == Block.HIT_RIGHT) {
                            collideToRightBlock = true;
                        } else if (hitCode == Block.HIT_BOTTOM) {
                            collideToBottomBlock = true;
                        } else if (hitCode == Block.HIT_LEFT) {
                            collideToLeftBlock = true;
                        } else if (hitCode == Block.HIT_TOP) {
                            collideToTopBlock = true;
                        } else if (hitCode == Block.HIT_TOP_LEFT) {
                            collideToTopBlock = true;
                            collideToLeftBlock = true;
                        } else if (hitCode == Block.HIT_TOP_RIGHT) {
                            collideToTopBlock = true;
                            collideToRightBlock = true;
                        } else if (hitCode == Block.HIT_BOTTOM_LEFT) {
                            collideToBottomBlock = true;
                            collideToLeftBlock = true;
                        } else if (hitCode == Block.HIT_BOTTOM_RIGHT) {
                            collideToBottomBlock = true;
                            collideToRightBlock = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the volume for the game sound effects.
     *
     * @param volume The volume level.
     */
    public void setVolume(double volume){
        this.volume=volume;
    }

    /**
     * Handles physics-related updates during the game loop.
     */
    @Override
    public void onPhysicsUpdate() {
        sound.setVolume(volume);
        checkDestroyedCount();
        setPhysicsToBall();

        if (time - goldTime > 5000) {
            Platform.runLater(() -> ball.setFill(new ImagePattern(new Image("ball.png"))));
            isGoldStatus = false;
        }

        if(isMaxTime){
            if(xVerstappen>=sceneWidth){
                isMaxTime=false;
                removeMax();
            }
            xVerstappen+=5;
            Platform.runLater(() -> {
                verstappen.setX(xVerstappen);
                verstappen.setY(yVerstappen);
            });
        }

        for (Bonus choco : bonuses) {
            if (choco.y > sceneHeight || choco.taken) {
                choco.bonus.setVisible(false);//If bonus got taken or went beyond game boundary set it to invisible
                continue;
            }
            if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                score += 3;
                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }
    }

    /**
     * Updates the game time.
     *
     * @param time The current game time.
     */
    @Override
    public void onTime(long time) {
        this.time = time;
    }

    /**
     * Spawns a special block (Max Verstappen) during the game.
     *
     * @param row        The row where the block is spawned.
     * @param difficulty The game difficulty level.
     */
    private void spawnMax(int row, int difficulty) {
        sound.pauseBGM();
        sound.playVerstappen("verstappen.mp3");
        isMaxTime=true;
        Platform.runLater(() -> {
            xVerstappen = 0 - breakWidth;
            yVerstappen = Block.getPaddingTop() + row * Block.getHeight();
            verstappen = new Rectangle();
            verstappen.setWidth(breakWidth);
            verstappen.setHeight(Block.getHeight());
            ImagePattern pattern = new ImagePattern(new Image("maxverstappen.png"));
            verstappen.setFill(pattern);
            root.getChildren().add(verstappen);
        });
    }

    /**
     * Removes the special block (Max Verstappen) from the game.
     */
    private void removeMax() {
        sound.stopVerstappen();
        sound.resumeBGM();
        xVerstappen=0;
        yVerstappen=0;
        Platform.runLater(() -> root.getChildren().remove(verstappen));
    }

    /**
     * Quits the game, stopping the game engine and closing the application window.
     */
    public void quit() {
        engine.stop();
        Platform.runLater(() -> {
            primaryStage.close();
        });
    }
}
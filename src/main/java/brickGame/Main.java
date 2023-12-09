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

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private final Object lock = new Object();
    private int level = 0;
    private double xBreak = 205.0f;
    private double centerBreakX;
    private double yBreak = 930.0f;
    private double xVerstappen = 0;
    private double yVerstappen = 0;
    private int breakWidth     = 130;
    private int breakHeight    = 30;
    private int halfBreakWidth = breakWidth / 2;
    public int sceneWidth = 540;
    public int sceneHeight = 960;
    private static int LEFT  = 1;
    private static int RIGHT = 2;
    private Circle ball;
    private double xBall;
    private double yBall;
    private double volume = 0.5;
    private boolean isInLevel = false;
    private boolean isMaxTime = false;
    private boolean isGoldStatus = false;
    private boolean isExistHeartBlock = false;
    private boolean isExistVerstappenBlock = false;
    private Rectangle rect;
    private Rectangle verstappen;
    private final int ballRadius = 10;
    private int destroyedBlockCount = 0;
    private int  difficulty    = 0;
    private int  heart    = 3;
    private int  score    = 0;
    public long time     = 0;
    private long goldTime = 0;
    private boolean goDownBall                   = false;
    private boolean goRightBall                  = true;
    private boolean collideToBreak               = false;
    private boolean collideToBreakAndMoveToRight = true;
    private boolean collideToRightWall           = false;
    private boolean collideToLeftWall            = false;
    private boolean collideToRightBlock          = false;
    private boolean collideToBottomBlock         = false;
    private boolean collideToLeftBlock           = false;
    private boolean collideToTopBlock            = false;
    private GameEngine engine = GameEngine.getInstance();
    public static String savePath    = "./save/save.mdds";
    public static String savePathDir = "./save/";
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
    private final Color[] colors = new Color[]{
            Color.rgb(173, 216, 230), // Pastel Blue
            Color.rgb(144, 238, 144), // Pastel Green
            Color.rgb(255, 255, 153), // Pastel Yellow
            Color.rgb(230, 230, 250), // Pastel Lavender
            Color.rgb(255, 218, 185), // Pastel Peach
            Color.rgb(200, 162, 200)  // Pastel Lilac
    };
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private boolean loadFromSave = false;
    private double vX = 1.000;
    private double vY = 1.000;
    private double defaultvX = 1.000;
    private double defaultvY = 1.000;
    private double breakSpeed = 4.000;
    public int maxLevel;
    public Stage  primaryStage = null;
    Sound sound = Sound.getInstance();
    Image backgroundImage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        StartMenu startMenu = new StartMenu(this);
        startMenu.show(this);
    }

    public void setDifficulty(int difficulty){
        this.difficulty=difficulty;
        maxLevel = 8*(difficulty+1)+1;
        breakSpeed=5-difficulty;
        defaultvX = 1 + difficulty;
        defaultvY = 1 + difficulty;
        sceneWidth=540+difficulty*Block.getWidth();
        backgroundImage = new Image("/bg"+ difficulty +".jpg");
    }

    public void startLevel(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        root = new Pane();

        if (loadFromSave == false) {
            level++;
            if (level >1){
                new Score().showMessage("Level Up :)", this);
            }
            if (level == maxLevel) {
                gameEnd();
                return;
            }
            initBall();
            initBreak();
            initBoard();
        }
        else {
            if (isGoldStatus){
                ball.setFill(new ImagePattern(new Image("goldball.png")));
            }
            loadFromSave = false;
        }

        engine = GameEngine.getInstance();
        engine.setOnAction(this);
        engine.setFps(120);
        if(!loadFromSave) {
            engine.start();
        }
        else{
            engine.start(time);
        }
        setStage(root);
    }

    public void setStage(Pane root){
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

    public void gameEnd(){
        sound.playGameOver("outro.mp3");
        sceneWidth=960;
        isInLevel=false;
        sound.stopBGM();
        root = new Pane();
        setScene(root);
        new GameEndMenu().showGameEnd(this, level, score);
    }

    private void setScene(Pane root){
        root.setStyle("-fx-background-image: url('"+backgroundImage.getUrl()+"');-fx-background-size: cover;");
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private void initBall() {
        Random random = new Random();
        xBall = random.nextInt(sceneWidth) + 1;
        yBall = random.nextInt(sceneHeight - 150 - ((level/(difficulty+1) + 1) * Block.getHeight())) + ((level/(difficulty+1) + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball2.png")));
    }

    private void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);
        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
        rect.setFill(pattern);
    }

    private void initBoard() {
        for (int i = 0; i < 4+difficulty; i++) {
            for (int j = 0; j < level/(difficulty+1) + 1; j++) {
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
                }else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

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

    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            nextLevel();
        }
    }

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

    public void setVolume(double volume){
        this.volume=volume;
    }

    @Override
    public void onPhysicsUpdate() {
        sound.setVolume(volume);
        checkDestroyedCount();
        setPhysicsToBall();

        if (time - goldTime > 5000) {
            Platform.runLater(() -> ball.setFill(new ImagePattern(new Image("ball2.png"))));
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

    @Override
    public void onTime(long time) {
        this.time = time;
    }

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

    private void removeMax() {
        sound.stopVerstappen();
        sound.resumeBGM();
        xVerstappen=0;
        yVerstappen=0;
        Platform.runLater(() -> root.getChildren().remove(verstappen));
    }

    public void quit() {
        engine.stop();
        Platform.runLater(() -> {
            primaryStage.close();
        });
    }
}
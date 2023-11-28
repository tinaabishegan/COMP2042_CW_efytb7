package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private final Object lock = new Object();
    private int level = -1;
    private double xBreak = 205.0f;
    private double centerBreakX;
    private double yBreak = 900.0f;
    private int breakWidth     = 130;
    private int breakHeight    = 30;
    private int halfBreakWidth = breakWidth / 2;
    private int sceneWidth = 540;
    private int sceneHeight = 960;
    private static int LEFT  = 1;
    private static int RIGHT = 2;
    private Circle ball;
    private double xBall;
    private double yBall;
    private boolean isGoldStatus = false;
    private boolean isExistHeartBlock = false;
    private Rectangle rect;
    private int ballRadius = 10;
    private int destroyedBlockCount = 0;
    private double v = 1.000;
    private int  difficulty    = 0;
    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;
    private long hitTime  = 0;
    private long goldTime = 0;
    private boolean goDownBall                   = true;
    private boolean goRightBall                  = true;
    private boolean collideToBreak               = false;
    private boolean collideToBreakAndMoveToRight = true;
    private boolean collideToRightWall           = false;
    private boolean collideToLeftWall            = false;
    private boolean collideToRightBlock          = false;
    private boolean collideToBottomBlock         = false;
    private boolean collideToLeftBlock           = false;
    private boolean collideToTopBlock            = false;
    private GameEngine engine;
    public static String savePath    = "./save/save.mdds";
    public static String savePathDir = "./save/";
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private Color[]          colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;
    private Label            difficultyLabel;
    private boolean loadFromSave = false;
    private double vX = 5.000;
    private double vY = 5.000;
    Stage  primaryStage;
    Button load    = null;
    Button newGame = null;
    Button diff1 = null;
    Button diff2 = null;
    Button diff3 = null;
    Sound sound = new Sound();

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new Pane();
        this.primaryStage = primaryStage;
        if(level<0){
            load = new Button("Load Game");
            newGame = new Button("Start New Game");
            diff1 = new Button("Easy");
            diff2 = new Button("Medium");
            diff3 = new Button("Hard");
            load.setTranslateX(100);
            load.setTranslateY(400);
            diff1.setTranslateX(100);
            diff1.setTranslateY(375);
            diff2.setTranslateX(139);
            diff2.setTranslateY(375);
            diff3.setTranslateX(199);
            diff3.setTranslateY(375);
            newGame.setTranslateX(100);
            newGame.setTranslateY(350);
            root.getChildren().addAll(load, newGame, diff1, diff2, diff3);

            diff1.setOnAction(event -> {
                difficulty=0;
            });
            diff2.setOnAction(event -> {
                difficulty=2;
            });

            diff3.setOnAction(event -> {
                difficulty=4;
            });
            load.setOnAction(event -> {
                sound.playBGM("running.mp3",0.01);
                initBall();
                initBreak();
                loadGame();
            });
            newGame.setOnAction(event -> {
                level++;
                sound.playBGM("running.mp3",0.01);
                sceneWidth=540+difficulty*Block.getWidth();
                try {
                        start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else{
            if (loadFromSave == false) {
                level++;
                if (level >1){
                    new Score().showMessage("Level Up :)", this);
                }
                if (level == 18) {
                    sound.stopBGM();
                    new Score().showWin(this);
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

            scoreLabel = new Label("Score: " + score);
            levelLabel = new Label("Level: " + level);
            levelLabel.setTranslateY(20);
            heartLabel = new Label("Heart : " + heart);
            heartLabel.setTranslateX(sceneWidth - 90);
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
            for (Block block : blocks) {
                root.getChildren().add(block.rect);
            }
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
        }

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void initBall() {
        Random random = new Random();
        xBall = random.nextInt(sceneWidth) + 1;
        yBall = random.nextInt(sceneHeight - 150 - ((level + 1) * Block.getHeight())) + ((level + 1) * Block.getHeight()) + 15;
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
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                //System.out.println("colors " + r % (colors.length));
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
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
        }
    }
    private void move(final int direction) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(30); // Run for 30 cycles

        KeyFrame moveFrame = new KeyFrame(Duration.millis(4), event -> {
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
        //v = ((time - hitTime) / 1000.000) + 1.000;
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
            //vX = 1.000;
            collideToLeftWall = true;
        }

        if (xBall + ballRadius >= sceneWidth) {
            resetCollideFlags();
            //vX = 1.000;
            collideToRightWall = true;
        }

        if (yBall - ballRadius <= 0) {
            //vX = 1.000;
            resetCollideFlags();
            goDownBall = true;
            return;
        }

        if (yBall + ballRadius >= sceneHeight) {
            Random random = new Random();
            //yBall= random.nextInt(100) + 100;
            resetCollideFlags();
            goDownBall = false;
            if (!isGoldStatus) {
                //TODO gameover
                heart--;
                new Score().show(sceneWidth / 2, sceneHeight / 2, -1, this);
                if (heart == 0) {
                    Platform.runLater(() -> {
                        sound.stopBGM();
                        heartLabel.setText("Heart : " + heart);
                        new Score().showGameOver(this, level, score);
                    });
                    nextLevel();
                }
            }
            return;
        }

        if (yBall >= yBreak - ballRadius && yBall <= yBreak + breakHeight + ballRadius){
            //System.out.println("collide1");
            if (xBall >= xBreak - ballRadius && xBall <= xBreak + breakWidth + ballRadius) {
                hitTime = time;
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / (breakWidth / 2);

                if (Math.abs(relation) <= 0.3) {
                    //vX = 0;
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                    //System.out.println("vX " + vX);
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);
                    //System.out.println("vX " + vX);
                }

                if (xBall - centerBreakX > 0) {
                    collideToBreakAndMoveToRight = true;
                } else {
                    collideToBreakAndMoveToRight = false;
                }
                //System.out.println("collide2");
            }
        }

        if (collideToBreak) {
            if (collideToBreakAndMoveToRight) {
                goRightBall = true;
            } else {
                goRightBall = false;
            }
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
    private void loadGame() {

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
        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }

        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void resetStage() {
        vX = 1.000;
        xBreak=205;
        resetCollideFlags();
        goDownBall = true;
        isGoldStatus = false;
        isExistHeartBlock = false;
        hitTime = 0;
        time = 0;
        goldTime = 0;
        blocks.clear();
        chocos.clear();
        destroyedBlockCount = 0;
    }
    private void checkDestroyedCount() {
        // System.out.println("Destroyed block count is "+ destroyedBlockCount + "\nBlock size is " + blocks.size());
        if (destroyedBlockCount == blocks.size()) {
            //endLevel();
            nextLevel();
        }
    }
    private void nextLevel() {
        resetStage();
        Platform.runLater(() -> {
            try {
                engine.stop();
                if(heart!=0) {
                    start(primaryStage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void restartGame() {
        difficulty=0;
        level = -1;
        heart = 3;
        score = 0;
        resetStage();
        Platform.runLater(() -> {
            try {
                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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

                for (Bonus choco : chocos) {
                    choco.choco.setY(choco.y);
                }
            });
        }




        synchronized (lock) {
            if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
                for (final Block block : blocks) {
                    int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                    if (hitCode != Block.NO_HIT) {
                        sound.playSFX("doorhit.mp3",0.01);
                        score += 1;

                        new Score().show(block.x, block.y, 1, this);

                        Platform.runLater(() -> block.rect.setVisible(false));
                        block.isDestroyed = true;
                        destroyedBlockCount++;
                        //System.out.println("size is " + blocks.size());
                        resetCollideFlags();

                        if (block.type == Block.BLOCK_CHOCO) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = time;
                            Platform.runLater(() -> root.getChildren().add(choco.choco));
                            chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) {
                            goldTime = time;
                            ball.setFill(new ImagePattern(new Image("goldball.png")));
                            System.out.println("gold ball");
                            new Score().showMessage("GOLDEN", this);
                            //root.getStyleClass().add("goldRoot");
                            isGoldStatus = true;
                        }

                        if (block.type == Block.BLOCK_HEART) {
                            heart++;
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
                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }

    }
    @Override
    public void onInit() {

    }
    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();


        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball2.png")));
            isGoldStatus = false;
        }

        for (Bonus choco : chocos) {
            if (choco.y > sceneHeight || choco.taken) {
                choco.choco.setVisible(false);//If bonus got taken or went beyond game boundary set it to invisible
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

        //System.out.println("time is:" + time + " goldTime is " + goldTime);

    }
    @Override
    public void onTime(long time) {
        this.time = time;
    }
}



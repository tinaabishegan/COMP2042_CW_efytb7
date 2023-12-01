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
import javafx.stage.Popup;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private final Object lock = new Object();
    private int level = 0;
    private double xBreak = 205.0f;
    private double centerBreakX;
    private double yBreak = 900.0f;
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
    private Color[] colors = new Color[]{
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
    public void start(Stage primaryStage) {
        // Create a VBox
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(10));



        this.primaryStage = primaryStage;
        load = new Button("Load Game");
        Button newGame = new Button("Start New Game");
        Button difficultyButton = new Button("Difficulty: Easy");
        Button helpmenu = new Button("How To Play");
        Button quit = new Button("Quit Game");
        vbox.getChildren().addAll(newGame, load, difficultyButton, helpmenu, quit);

        HBox hbox = new HBox();
        hbox.setId("rootMenu");
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().add(vbox);
        hbox.setPadding(new Insets(10));

        Scene scene = new Scene(hbox, sceneWidth, sceneHeight);

        difficultyButton.setOnAction(event -> {
            // Update the button text based on the new difficulty level
            switch (difficulty) {
                case 0:
                    difficultyButton.setText("Difficulty: Medium");
                    difficulty=2;
                    break;
                case 2:
                    difficultyButton.setText("Difficulty: Hard");
                    difficulty=4;
                    break;
                case 4:
                    difficultyButton.setText("Difficulty: Easy");
                    difficulty=0;
                    break;
                // Add more cases if you have additional difficulty levels
            }
        });

        load.setOnAction(event -> {
            sound.playBGM("running.mp3");
            initBall();
            initBreak();
            loadGame();
        });

        newGame.setOnAction(event -> {
            sound.playBGM("running.mp3");
            sceneWidth=540+difficulty*Block.getWidth();
            try {
                isInLevel=true;
                startLevel(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        helpmenu.setOnAction(event -> {
            showHelp(primaryStage);
        });

        quit.setOnAction(event -> {
            primaryStage.close();
        });

        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }


    public void startLevel(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        root = new Pane();
        Image backgroundImage = new Image("/bg"+ difficulty +".jpg");
        root.setStyle("-fx-background-image: url('"+backgroundImage.getUrl()+"');-fx-background-size: cover;");
        if (loadFromSave == false) {
            level++;
            if (level >1){
                new Score().showMessage("Level Up :)", this);
            }
            if (level == 18) {
                isInLevel=true;
                sound.stopBGM();
                new Score().showGameEnd(this, level, score);
                setScene();
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
        if(!loadFromSave) {
            engine.start();
        }
        else{
            engine.start(time);
        }

        setScene();
    }

    public void gameEnd(){
        sound.stopBGM();
        root = new Pane();
        VBox pauseRoot = new VBox(5);




        setScene();
    }

    private void setScene(){
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
            case P:
                pauseGame();
                break;
        }
    }
    private void move(final int direction) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(30); // Run for 30 cycles

        KeyFrame moveFrame = new KeyFrame(Duration.millis(2), event -> {
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
                        isInLevel=false;
                        gameEnd();
                        new Score().showGameEnd(this, level, score);
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
            isInLevel=true;
            loadFromSave = true;
            startLevel(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void resetStage() {
        if(isMaxTime){
            isMaxTime=false;
            removeMax();
        }
        vX = 1.000;
        xBreak=205;
        resetCollideFlags();
        goDownBall = true;
        isGoldStatus = false;
        isExistHeartBlock = false;
        isExistVerstappenBlock=false;
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
    public void restartGame() {
        difficulty=0;
        sceneWidth=540;
        level = 0;
        heart = 3;
        score = 0;
        Platform.runLater(() -> {
            try {
                resetStage();
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

            if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop() || isMaxTime) {
                for (final Block block : blocks) {
                    int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                    int maxCode = block.checkHitToBlock(xVerstappen+breakWidth, yVerstappen+15,ballRadius);
                    if (hitCode != Block.NO_HIT || maxCode != Block.NO_HIT) {
                        sound.playSFX("doorhit.mp3");
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
        sound.setVolume(volume);
        checkDestroyedCount();
        setPhysicsToBall();

        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball2.png")));
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

    private void pauseGame(){
        if(isInLevel){
            VBox pauseRoot = new VBox(5);
            pauseRoot.setId("pauseMenu");
            Label paused = new Label("Paused");
            paused.setPadding(new Insets(0, 0, 60, 0));
            pauseRoot.setAlignment(Pos.CENTER);
            pauseRoot.setPadding(new Insets(25, 100, 120, 100));

            Button resume = new Button("Resume");
            Button main_menu = new Button("Return to Main Menu");
            Button quit = new Button("Quit Game");
            Button help = new Button("How to Play?");

            Label volumeLabel = new Label("Volume: "+ volume);
            Slider volumeSlider = new Slider(0, 1, 0.5); // Min, Max, Default
            volumeSlider.setShowTickLabels(true);
            volumeSlider.setShowTickMarks(true);
            volumeSlider.setMajorTickUnit(0.1); // Major tick every 0.1
            volumeSlider.setMinorTickCount(11);
            volumeSlider.setSnapToTicks(true);
            volumeSlider.setValue(volume);
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double snapToTickValue = Math.round(newVal.doubleValue() / 0.1) * 0.1; // Assuming major tick unit is 0.1
                volumeSlider.setValue(snapToTickValue);
            });
            // Add a listener to the slider to update the volume label
            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                volume = newValue.doubleValue();
                volumeLabel.setText(String.format("Volume: %.1f", volume));

                // Add your logic to update the actual volume (e.g., set volume in a media player)
                // For this example, we're just printing the volume value
                System.out.println("New Volume: " + volume);
            });

            pauseRoot.getChildren().addAll(paused, resume, main_menu, help, quit, volumeLabel, volumeSlider);

            Stage popupStage = new Stage(StageStyle.TRANSPARENT);
            popupStage.initOwner(primaryStage);
            //popupStage.initModality(Modality.APPLICATION_MODAL);

            Scene scenePause = new Scene(pauseRoot, Color.TRANSPARENT);
            scenePause.getStylesheets().add("style.css");
            popupStage.setScene(scenePause);

            resume.setOnAction(event -> {
                sound.setVolume(volume);
                popupStage.hide();
                engine.start();
                sound.resumeBGM();
                sound.resumeVerstappen();
            });

            main_menu.setOnAction(event -> {
                popupStage.hide();
                sound.stopBGM();
                engine.stop();
                restartGame();
            });

            help.setOnAction(event -> {
                showHelp(popupStage);
            });

            quit.setOnAction(event -> {
                quit();
            });




            engine.stop();
            sound.pauseBGM();
            sound.pauseVerstappen();
            popupStage.show();
        }
    }

    private void spawnMax(int row, int difficulty) {
        sound.pauseBGM();
        sound.playVerstappen("verstappen.mp3");
        isMaxTime=true;
        System.out.println("max");
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
        primaryStage.close();
    }

    public void showHelp(Stage stage){
        Image backgroundImage = new Image("help.png");

        Popup popup = new Popup();

        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(500);
        backgroundImageView.setFitHeight(629);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popup.hide());

        VBox popupLayout = new VBox(10);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.getChildren().addAll(backgroundImageView, closeButton);

        popup.getContent().add(popupLayout);

        // Show the popup below the owner button
        popup.show(stage, 710, 140);
    }
}

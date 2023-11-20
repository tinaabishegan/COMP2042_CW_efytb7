package brickGame;

public class GameEngineold {

    private OnAction onAction;
    private int fps = 60;
    private long frameTime = 1000000000 / fps;
    private Thread updateThread;
    private Thread physicsThread;
    private Thread timeThread;
    private volatile boolean isStopped = true;
    private long time = 0;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    public void setFps(int fps) {
        this.fps = fps;
        this.frameTime = 1000000000 / fps;
    }

    private void initialize() {
        onAction.onInit();
    }

    private void updateLoop() {
        long lastTime = System.nanoTime();
        long unprocessedTime = 0;

        while (!Thread.interrupted()) {
            long now = System.nanoTime();
            long passedTime = now - lastTime;
            lastTime = now;
            unprocessedTime += passedTime;

            while (unprocessedTime >= frameTime) {
                onAction.onUpdate();
                unprocessedTime -= frameTime;
            }

            Thread.yield();
        }
    }

    private void physicsLoop() {
        long lastTime = System.nanoTime();
        long unprocessedTime = 0;

        while (!Thread.interrupted()) {
            long now = System.nanoTime();
            long passedTime = now - lastTime;
            lastTime = now;
            unprocessedTime += passedTime;

            while (unprocessedTime >= frameTime) {
                onAction.onPhysicsUpdate();
                unprocessedTime -= frameTime;
            }

            Thread.yield();
        }
    }

    private void timeStart() {
        timeThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            long unprocessedTime = 0;

            while (!Thread.interrupted()) {
                long now = System.nanoTime();
                long passedTime = now - lastTime;
                lastTime = now;
                unprocessedTime += passedTime;

                while (unprocessedTime >= frameTime) {
                    time++;
                    onAction.onTime(time);
                    unprocessedTime -= frameTime;
                }

                Thread.yield();
            }
        });
        timeThread.start();
    }

    public void start() {
        if (isStopped) {
            time = 0;
            initialize();
            updateThread = new Thread(this::updateLoop);
            physicsThread = new Thread(this::physicsLoop);
            updateThread.start();
            physicsThread.start();
            timeStart();
            isStopped = false;
        }
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();
        }
    }

    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}

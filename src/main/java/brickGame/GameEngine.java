package brickGame;


public class GameEngine {
    private static GameEngine instance;
    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    public volatile boolean isStopped = true;

    private GameEngine() {
        // Private constructor to prevent instantiation outside of this class
    }

    // Singleton instance retrieval method
    public static synchronized GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps Set frames per second and convert it to milliseconds
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps;
    }

    // Method to start update Thread
    private synchronized void Update() {
        updateThread = new Thread(() -> {
            // Continue running until the thread is interrupted
            while (!updateThread.isInterrupted()) {
                try {
                    onAction.onUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    System.out.println("onUpdate thread interrupted.");
                    Thread.currentThread().interrupt();
                }
            }
        });
        updateThread.start();
    }


    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(() -> {
            while (!physicsThread.isInterrupted()) {
                try {
                    onAction.onPhysicsUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    System.out.println("onPhysics thread interrupted.");
                    Thread.currentThread().interrupt();
                }
            }
        });

        physicsThread.start();

    }

    public void start() {
        time = 0;
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }
    public void start(long t) {
        time = t;
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();
        }
    }

    private long time = 0;

    private Thread timeThread;

    private synchronized void  TimeStart() {
        timeThread = new Thread(() -> {
            try {
                while (true) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                System.out.println("TimeStart thread interrupted.");
                Thread.currentThread().interrupt();
            }
        });
        timeThread.start();
    }


    public interface OnAction {
        void onUpdate();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}

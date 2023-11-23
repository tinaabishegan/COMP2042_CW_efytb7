package brickGame;


public class GameEngine {

    // Interface for actions to be performed in the game
    private OnAction onAction;

    // Frames per second ( set to 15 )
    private int fps = 15;

    // Thread for game update
    private Thread updateThread;

    // Thread for physics calculations
    private Thread physicsThread;

    // Flag to check if game is stopped
    public volatile boolean isStopped = true;

    // Set the action handler for the game
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

    private void Initialize() {
        onAction.onInit();
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
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }
    public void start(long t) {
        time = t;
        Initialize();
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

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}

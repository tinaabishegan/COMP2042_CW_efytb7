package brickGame;

/**
 * The GameEngine class represents the core engine for a simple game.
 * It follows the Singleton pattern, ensuring that only one instance of the class can exist.
 */
public class GameEngine {

    /** The singleton instance of the GameEngine. */
    private static GameEngine instance;

    /** The callback for handling game actions. */
    private OnAction onAction;

    /** Frames per second (fps) setting for the game. */
    private int fps = 15;

    /** The thread responsible for updating the game state. */
    private Thread updateThread;

    /** The thread responsible for handling physics calculations. */
    private Thread physicsThread;

    /** Flag indicating whether the game engine is stopped. */
    public volatile boolean isStopped = true;

    /** The current time in the game. */
    private long time = 0;

    /** The thread responsible for managing game time. */
    private Thread timeThread;

    // Private constructor to prevent instantiation outside of this class
    private GameEngine() {
    }

    /**
     * Retrieves the singleton instance of the GameEngine.
     *
     * @return The singleton instance of the GameEngine.
     */
    public static synchronized GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    /**
     * Sets the callback for various game actions.
     *
     * @param onAction The callback interface implementation.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the frames per second and converts it to milliseconds.
     *
     * @param fps The desired frames per second.
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps;
    }

    /**
     * Starts the update and physics calculation threads along with the time tracking thread.
     */
    public void start() {
        time = 0;
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    /**
     * Starts the engine with an initial time value.
     *
     * @param t The initial time value.
     */
    public void start(long t) {
        time = t;
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    /**
     * Stops the game engine and interrupts all running threads.
     */
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();
        }
    }

    // Private method to start the update thread
    private synchronized void Update() {
        updateThread = new Thread(() -> {
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

    // Private method to start the physics calculation thread
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

    // Private method to start the time tracking thread
    private synchronized void TimeStart() {
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

    /**
     * Callback interface for various game actions.
     */
    public interface OnAction {
        /**
         * Called on each update cycle.
         */
        void onUpdate();

        /**
         * Called on each physics update cycle.
         */
        void onPhysicsUpdate();

        /**
         * Called on each time update cycle.
         *
         * @param time The current time value.
         */
        void onTime(long time);
    }
}

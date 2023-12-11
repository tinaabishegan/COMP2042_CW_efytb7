package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * The Sound class manages the game's audio features, including background music,
 * sound effects, and game over music. It follows the Singleton pattern, ensuring
 * a single instance of the class throughout the application.
 */
public class Sound {
    private static Sound instance;
    private MediaPlayer bgmPlayer;
    private MediaPlayer verstappenPlayer;
    private MediaPlayer gameOverPlayer;
    private double volume = 0.5;

    /**
     * Private constructor to prevent direct instantiation of the class.
     */
    private Sound() {
    }

    /**
     * Returns the singleton instance of the Sound class.
     *
     * @return The Sound instance.
     */
    public static Sound getInstance() {
        if (instance == null) {
            instance = new Sound();
        }
        return instance;
    }

    /**
     * Sets the volume level for all audio components.
     *
     * @param volume The volume level (0.0 to 1.0).
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * Gets the current volume level.
     *
     * @return The current volume level.
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Plays the specified Verstappen audio in a loop.
     *
     * @param backgroundMusicPath The path to the Verstappen audio file.
     */
    public void playVerstappen(String backgroundMusicPath) {
        if (verstappenPlayer != null) {
            verstappenPlayer.stop();
        }

        try {
            URL resource = getClass().getResource("/sounds/" + backgroundMusicPath);
            if (resource == null) {
                throw new IllegalArgumentException("Cannot find file: " + backgroundMusicPath);
            }

            Media backgroundMusic = new Media(resource.toExternalForm());
            verstappenPlayer = new MediaPlayer(backgroundMusic);
            verstappenPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            verstappenPlayer.setVolume(volume);
            verstappenPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing Verstappen audio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Pauses the Verstappen audio if it is currently playing.
     */
    public void pauseVerstappen() {
        if (verstappenPlayer != null && verstappenPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            verstappenPlayer.pause();
        }
    }

    /**
     * Resumes the Verstappen audio if it was paused.
     */
    public void resumeVerstappen() {
        if (verstappenPlayer != null && verstappenPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            verstappenPlayer.setVolume(volume);
            verstappenPlayer.play();
        }
    }

    /**
     * Stops the Verstappen audio.
     */
    public void stopVerstappen() {
        verstappenPlayer.stop();
    }

    /**
     * Plays the specified background music in a loop.
     *
     * @param backgroundMusicPath The path to the background music file.
     */
    public void playBGM(String backgroundMusicPath) {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }

        try {
            URL resource = getClass().getResource("/sounds/" + backgroundMusicPath);
            if (resource == null) {
                throw new IllegalArgumentException("Cannot find file: " + backgroundMusicPath);
            }

            Media backgroundMusic = new Media(resource.toExternalForm());
            bgmPlayer = new MediaPlayer(backgroundMusic);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(volume);
            bgmPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Pauses the background music if it is currently playing.
     */
    public void pauseBGM() {
        if (bgmPlayer != null && bgmPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            bgmPlayer.pause();
        }
    }

    /**
     * Resumes the background music if it was paused.
     */
    public void resumeBGM() {
        if (bgmPlayer != null && bgmPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            bgmPlayer.setVolume(volume);
            bgmPlayer.play();
        }
    }

    /**
     * Stops the background music.
     */
    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    /**
     * Plays the specified sound effect.
     *
     * @param soundEffectPath The path to the sound effect file.
     */
    public void playSFX(String soundEffectPath) {
        try {
            URL resource = getClass().getResource("/sounds/" + soundEffectPath);
            if (resource == null) {
                throw new IllegalArgumentException("Cannot find file: " + soundEffectPath);
            }

            Media soundEffect = new Media(resource.toExternalForm());
            MediaPlayer sfxPlayer = new MediaPlayer(soundEffect);
            sfxPlayer.setVolume(volume);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing sound effect: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Plays the specified game over music in a loop.
     *
     * @param gameOverMusicPath The path to the game over music file.
     */
    public void playGameOver(String gameOverMusicPath) {
        if (gameOverPlayer != null) {
            gameOverPlayer.stop();
        }

        try {
            URL resource = getClass().getResource("/sounds/" + gameOverMusicPath);
            if (resource == null) {
                throw new IllegalArgumentException("Cannot find file: " + gameOverMusicPath);
            }

            Media backgroundMusic = new Media(resource.toExternalForm());
            gameOverPlayer = new MediaPlayer(backgroundMusic);
            gameOverPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            gameOverPlayer.setVolume(volume);
            gameOverPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing game over music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Stops the game over music.
     */
    public void stopGameOver() {
        gameOverPlayer.stop();
    }
}
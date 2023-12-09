package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class Sound {
    private static Sound instance;
    private MediaPlayer bgmPlayer;
    private MediaPlayer verstappenPlayer;
    private MediaPlayer gameOverPlayer;
    private double volume = 0.5;

    private Sound() {
    }

    public static Sound getInstance() {
        if (instance == null) {
            instance = new Sound();
        }
        return instance;
    }

    public void setVolume(double volume){
        this.volume=volume;
    }

    public double getVolume(){
        return volume;
    }

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
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void pauseVerstappen() {
        if (verstappenPlayer != null && verstappenPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            verstappenPlayer.pause();
        }
    }
    public void resumeVerstappen() {
        if (verstappenPlayer != null && verstappenPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            verstappenPlayer.setVolume(volume);
            verstappenPlayer.play();
        }
    }
    public void stopVerstappen(){
        verstappenPlayer.stop();
    }
    public void playBGM(String backgroundMusicPath) {
        // If there's already music playing, stop it
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
    public void pauseBGM() {
        if (bgmPlayer != null && bgmPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            bgmPlayer.pause();
        }
    }
    public void resumeBGM() {
        if (bgmPlayer != null && bgmPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            bgmPlayer.setVolume(volume);
            bgmPlayer.play();
        }
    }
    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

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

    public void stopGameOver(){
        gameOverPlayer.stop();
    }

}
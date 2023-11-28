package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
public class Sound {
    private MediaPlayer bgmPlayer;
    private MediaPlayer sfxPlayer;


    public void playBGM(String backgroundMusicPath, double volume) {
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

    public void playSFX(String soundEffectPath, double volume) {
        try {
            URL resource = getClass().getResource("/sounds/" + soundEffectPath);
            if (resource == null) {
                throw new IllegalArgumentException("Cannot find file: " + soundEffectPath);
            }

            Media soundEffect = new Media(resource.toExternalForm());
            sfxPlayer = new MediaPlayer(soundEffect);
            sfxPlayer.setVolume(volume);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing sound effect: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopBGM(){
        bgmPlayer.stop();
    }

}
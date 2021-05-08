package com.unizar.game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Manages the game sounds
 */
public class Sound {

    private Clip playingClip = null;

    private String playingFile = null;

    /**
     * Plays a background music
     *
     * @param path music to play
     */
    public void backgroundMusic(String path) {
        if (Objects.equals(playingFile, path)) return;
        playingFile = path;


        // stop if playing
        stop();

        if (path == null) {
            // nothing to play
            playingClip = null;
            return;
        }

        try {

            // find file
            final AudioInputStream stream = AudioSystem.getAudioInputStream(new File(path));

            // create player
            playingClip = AudioSystem.getClip();
            playingClip.open(stream);
            playingClip.loop(playingClip.LOOP_CONTINUOUSLY);

            // play
            playingClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the playing music (if any)
     */
    public void stop() {
        if (playingClip != null)
            playingClip.stop();
    }
}

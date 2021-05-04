package com.unizar.game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import java.net.URL;

public class Sound {

    private Clip playingClip = null;

    /**
     * Plays a background music
     *
     * @param path music to play
     */
    public void backgroundMusic(String path) {

        // stop if playing
        if (playingClip != null)
            playingClip.stop();

        if (path == null) {
            // nothing to play
            playingClip = null;
            return;
        }

        try {

            // find file
            URL resource = Game.class.getResource(path);
            if (resource == null) {
                throw new IOException("The music '" + path + "' doesn't exist.");
            }
            final AudioInputStream stream = AudioSystem.getAudioInputStream(resource);

            // create player
            playingClip = AudioSystem.getClip();
            playingClip.open(stream);
            playingClip.loop(playingClip.LOOP_CONTINUOUSLY);

            // play
            setVolume(volume);
            playingClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float volume = 1;

    /**
     * Changes the volume of the music...or it should
     *
     * @param volume
     */
    public void setVolume(float volume) {
        // java is NOT a good language for games

//        this.volume = volume;
//        if (playingClip != null) {
//            FloatControl gainControl = (FloatControl) playingClip.getControl(FloatControl.Type.MASTER_GAIN);
//            float range = gainControl.getMaximum() - gainControl.getMinimum();
//            final double pow = Math.pow(volume, 5.0 / 1.0);
//            System.out.println(pow);
//            gainControl.setValue((float) (gainControl.getMinimum() + range * pow));
//        }
    }

}

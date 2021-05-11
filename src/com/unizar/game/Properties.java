package com.unizar.game;

import java.io.Serializable;

/**
 * Specific properties of a game
 */
abstract public class Properties implements Serializable {

    /**
     * @return the title of the game (window's title)
     */
    abstract public String getTitle();

    /**
     * @return the ratio of the images (WIDTH / HEIGHT)
     */
    public abstract int getImageRatio();

    /**
     * Return the path of an image
     *
     * @param label the label of the image
     * @return the path of the image
     */
    abstract public String getImagePath(String label);

    /**
     * Return the path of a music
     *
     * @param label the label of the music
     * @return the path of the music
     */
    public abstract String getMusicPath(String label);

    /**
     * @return the font to use
     */
    public abstract String getFontFile();

    /**
     * @return the start screen (image name)
     */
    public abstract String getStartScreen();

    /**
     * @return the win screen (image name)
     */
    public abstract String getWinScreen();

    /**
     * @return the description of the start screen
     */
    public abstract String getStartDescription();

    /**
     * @return the description of the win screen
     */
    public abstract String getWinDescription();

    /**
     * @return the path of the help file
     */
    public abstract String getHelpFile();

    /**
     * @return the music of the win screen
     */
    public abstract String getWinMusic();

    /**
     * @return the description of the game over screen
     */
    public abstract String getGameOverMusic();

    /**
     * @return the game over screen (image name)
     */
    public abstract String getGameOverScreen();

    /**
     * @return the description of the win screen
     */
    public abstract String getStartMusic();
}

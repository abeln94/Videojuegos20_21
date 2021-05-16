package com.unizar.generic;

import com.unizar.game.Properties;
import org.json.JSONObject;

/**
 * A properties file generated from a JSON object
 */
public class JSONProperties extends Properties {

    // TODO: use internally a JSONObject instead of a serializable field

    private final String title;

    private final int imageRatio;
    private final String fontFile;
    private final String helpFile;

    private final String imagePath;
    private final String musicPath;

    private final String startScreen;
    private final String startDescription;
    private final String startMusic;

    private final String winScreen;
    private final String winDescription;
    private final String winMusic;

    private final String gameOverScreen;
    private final String gameOverMusic;

    public JSONProperties(String root, JSONObject properties) {
        title = properties.optString("title", "My Game");
        imageRatio = properties.optInt("imageRatio", 1);
        imagePath = root + properties.optString("imagePath", "/{}");
        musicPath = root + properties.optString("musicPath", "/{}");
        fontFile = root + properties.optString("fontFile", "/{}");
        startScreen = properties.optString("startScreen", null);
        winScreen = properties.optString("winScreen", null);
        startDescription = properties.optString("startDescription", null);
        winDescription = properties.optString("winDescription", "You won");
        helpFile = root + properties.optString("helpFile", null);
        winMusic = properties.optString("winMusic", null);
        gameOverMusic = properties.optString("gameOverMusic", null);
        gameOverScreen = properties.optString("gameOverScreen", null);
        startMusic = properties.optString("startMusic", null);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getImageRatio() {
        return imageRatio;
    }

    @Override
    public String getImagePath(String label) {
        return imagePath.replace("{}", label);
    }

    @Override
    public String getMusicPath(String label) {
        return musicPath.replace("{}", label);
    }

    @Override
    public String getFontFile() {
        return fontFile;
    }

    @Override
    public String getStartScreen() {
        return startScreen;
    }

    @Override
    public String getWinScreen() {
        return winScreen;
    }

    @Override
    public String getStartDescription() {
        return startDescription;
    }

    @Override
    public String getWinDescription() {
        return winDescription;
    }

    @Override
    public String getHelpFile() {
        return helpFile;
    }

    @Override
    public String getWinMusic() {
        return winMusic;
    }

    @Override
    public String getGameOverMusic() {
        return gameOverMusic;
    }

    @Override
    public String getGameOverScreen() {
        return gameOverScreen;
    }

    @Override
    public String getStartMusic() {
        return startMusic;
    }
}

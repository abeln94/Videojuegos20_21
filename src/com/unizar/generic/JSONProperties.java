package com.unizar.generic;

import com.unizar.Main;
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

    // experimental
    private final String code;

    public JSONProperties(JSONObject properties) {
        title = properties.optString("title", "My Game");
        imageRatio = properties.optInt("imageRatio", 1);
        fontFile = Main.root + properties.optString("fontFile", "{}");
        helpFile = Main.root + properties.optString("helpFile", null);

        imagePath = Main.root + properties.optString("imagePath", "{}");
        musicPath = Main.root + properties.optString("musicPath", "{}");

        startScreen = properties.optString("startScreen", null);
        startDescription = properties.optString("startDescription", "");
        startMusic = properties.optString("startMusic", null);

        winScreen = properties.optString("winScreen", null);
        winDescription = properties.optString("winDescription", "You won");
        winMusic = properties.optString("winMusic", null);

        gameOverScreen = properties.optString("gameOverScreen", null);
        //TODO: add a gameOverDescription property
        gameOverMusic = properties.optString("gameOverMusic", null);

        code = properties.optString("code", null);
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
    public String getFontFile() {
        return fontFile;
    }

    @Override
    public String getHelpFile() {
        return helpFile;
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
    public String getStartScreen() {
        return startScreen;
    }

    @Override
    public String getStartDescription() {
        return startDescription;
    }

    @Override
    public String getStartMusic() {
        return startMusic;
    }

    @Override
    public String getWinScreen() {
        return winScreen;
    }

    @Override
    public String getWinDescription() {
        return winDescription;
    }

    @Override
    public String getWinMusic() {
        return winMusic;
    }

    @Override
    public String getGameOverScreen() {
        return gameOverScreen;
    }

    @Override
    public String getGameOverMusic() {
        return gameOverMusic;
    }

    public String getCode() {
        return code;
    }
}

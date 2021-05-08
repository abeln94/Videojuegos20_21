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
    private final String imagePath;
    private final String musicPath;
    private final String fontFile;
    private final String startScreen;
    private final String winScreen;
    private final String startDescription;
    private final String winDescription;
    private final String helpFile;

    public JSONProperties(String root, JSONObject properties) {

        title = properties.getString("title");
        imageRatio = properties.getInt("imageRatio");
        imagePath = root + properties.getString("imagePath");
        musicPath = root + properties.getString("musicPath");
        fontFile = root + properties.getString("fontFile");
        startScreen = properties.getString("startScreen");
        winScreen = properties.getString("winScreen");
        startDescription = properties.getString("startDescription");
        winDescription = properties.getString("winDescription");
        helpFile = root + properties.getString("helpFile");
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
}

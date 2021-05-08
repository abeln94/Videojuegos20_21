package com.unizar.hobbit;

import com.unizar.game.Properties;

public class HobbitProperties extends Properties {
    public String getTitle() {
        return "El hobbit";
    }

    @Override
    public int getImageRatio() {
        return 2;
    }

    public String getImagePath(String label) {
        return "/128k/" + label + ".PNG";
    }

    @Override
    public String getMusicPath(String label) {
        return "/music/" + label + ".wav";
    }

    @Override
    public String getFontFile() {
        return "/fonts/MorrisRoman-Black.ttf";
    }

    @Override
    public String getStartScreen() {
        return "1_español";
    }

    @Override
    public String getWinScreen() {
        return "48";
    }

    @Override
    public String getStartDescription() {
        return "Eres un hobbit, tu objetivo es derrotar al dragon y quedarte su tesoro.";
    }

    @Override
    public String getWinDescription() {
        return "Has conseguido derrotar al dragón y te has llevado su oro (que si, de verdad)!";
    }

    @Override
    public String getHelpPath() {
        return null;
    }
}

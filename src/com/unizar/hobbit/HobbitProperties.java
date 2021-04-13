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
    public String getFontName() {
        return "/fonts/MorrisRoman-Black.ttf";
    }

    @Override
    public String getStartScreen() {
        return "1_español";
    }

    @Override
    public String getDescription() {
        return "Eres un hobbit, tu objetivo es derrotar al dragon y quedarte su tesoro.";
    }
}

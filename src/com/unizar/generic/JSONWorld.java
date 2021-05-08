package com.unizar.generic;

import com.unizar.game.Game;
import com.unizar.game.World;

import java.io.IOException;

/**
 * Look! I can use JSON!
 */
public class JSONWorld extends World {

    public JSONWorld(String root) {
        // properties
        try {
            properties = DataLoader.loadProperties(root);
            elements = DataLoader.loadElements(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean playerWon(Game game) {
        return false;
    }
}

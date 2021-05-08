package com.unizar.hobbit;

import com.unizar.game.DataLoader;
import com.unizar.game.Game;
import com.unizar.game.World;

import java.io.IOException;

/**
 * Look! I can use JSON!
 */
public class HobbitWorldJson extends World {

    public HobbitWorldJson() throws IOException {
        // properties
        properties = DataLoader.loadProperties("hobbit");
        elements = DataLoader.loadElements("hobbit");
    }

    @Override
    public boolean playerWon(Game game) {
        return false;
    }
}

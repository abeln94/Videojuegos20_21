package com.unizar.hobbit;

import com.unizar.game.DataLoader;
import com.unizar.game.Game;
import com.unizar.game.World;

import java.io.IOException;

/**
 * This should be better with annotations, but from now lets use it explicitly
 */
public class HobbitWorldJson extends World {

    public HobbitWorldJson() throws IOException {
        // properties
        properties = new HobbitProperties();

        elements = DataLoader.loadElements("hobbit");

    }

    @Override
    public boolean playerWon(Game game) {
        return false;//game.findElementByClassName(StartChest.class).elements.contains(game.findElementByClassName(Gold.class));
    }
}

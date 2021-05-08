package com.unizar;

import com.unizar.game.DataLoader;
import com.unizar.game.elements.Element;

import java.io.IOException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {

//        Game game = new Game(new HobbitWorld());
//        game.reset();
//        DataLoader.saveElements("test", game.world.elements);

        final Set<Element> elements = DataLoader.loadElements("test", null);

//        // hobbit
//        new Game(new HobbitWorld());
    }
}

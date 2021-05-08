package com.unizar;

import com.unizar.game.Game;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

//        if (Arrays.asList(args).contains("save")) {
//            Game game = new Game(new HobbitWorld());
//            game.reset();
//            DataLoader.saveElements("hobbit", game.world.elements);
//            DataLoader.saveProperties("hobbit", game.world.properties);
//            game.exit();
//        }

//
//        final Set<Element> elements = DataLoader.loadElements("test");
//        elements.forEach(element -> element.register(game));

//        // hobbit
//        new Game(new HobbitWorld());

        new Game("hobbit");
    }
}

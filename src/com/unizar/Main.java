package com.unizar;

import com.unizar.generic.DataLoader;
import com.unizar.game.Game;
import com.unizar.hobbit.HobbitWorld;
import com.unizar.generic.JSONWorld;

import java.io.IOException;
import java.util.Arrays;

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

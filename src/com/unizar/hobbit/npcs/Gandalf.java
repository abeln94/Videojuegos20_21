package com.unizar.hobbit.npcs;

import com.unizar.game.NPC;

import java.util.Random;

public class Gandalf extends NPC {
    @Override
    public void act() {
        String[] actions = new String[]{
                "da una voltereta",
                "hace una pirueta",
                "te mira fijamente"
        };
        Random random = new Random();
        if (random.nextBoolean())
            game.addDescription("\nGandalf " + actions[random.nextInt(actions.length)]);
    }
}

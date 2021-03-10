package com.unizar.hobbit.npcs;

import com.unizar.game.NPC;

import java.util.Random;

public class Gandalf extends NPC {

    private boolean hasMap = true;

    public Gandalf() {
        setCurrentRoom("initial");
    }

    @Override
    public void act() {

        Random random = new Random();
        if (getCurrentRoom().equals(game.getData().getPlayer().getCurrentRoom()) && random.nextBoolean()) {
            // exchange map
            if (hasMap) {
                game.addDescription("Gandalf te da el mapa peculiar.");
                hasMap = false;
                ((Bilbo_Player) game.getData().getPlayer()).hasMap = false;
            } else {
                game.addDescription("Gandalf te coge el mapa peculiar.");
                hasMap = true;
                ((Bilbo_Player) game.getData().getPlayer()).hasMap = true;
            }
        }
    }
}

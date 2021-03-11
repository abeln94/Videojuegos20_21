package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Map;
import com.unizar.hobbit.rooms.StartRoom;

import java.util.Random;

public class Gandalf extends NPC {
    public Gandalf() {
        super("Gandalf", StartRoom.class);
    }

    @Override
    public void act() {

        actAsPlayer();

        // exchange map with player
        Random random = new Random();
        if (game.data.getPlayer().getHolder() == getHolder() && random.nextBoolean()) {
            Map map = game.data.getElement(Map.class);
            boolean hasMap = map.getHolder() == this.getClass();

            if (hasMap) {
                game.addOutput("Gandalf te da el mapa peculiar.");
                map.setHolder(game.data.getPlayer().getClass());
            } else {
                game.addOutput("Gandalf te coge el mapa peculiar.");
                map.setHolder(this.getClass());
            }
        }

        // open the door of the StartRoom
        // TODO: change to an 'open' command
        StartRoom startRoom = game.data.getElement(StartRoom.class);
        if (!startRoom.doorOpen && random.nextBoolean()) {
            startRoom.doorOpen = true;
            game.addOutput("Gandalf abre la puerta.");
        }

    }

    @Override
    public void describe() {
        boolean hasMap = game.data.getElement(Map.class).getHolder() == this.getClass();
        game.addDescription("Gandalf" + (hasMap ? ": Gandalf lleva un mapa peculiar" : ""));
    }
}

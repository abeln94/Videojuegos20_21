package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;
import com.unizar.game.elements.Player;
import com.unizar.hobbit.rooms.StartLocation;

public class Bilbo_Player extends Player {

    public Bilbo_Player() {
        super("Bilbo");
    }

    @Override
    public void init() {
        location = game.findElementByClassName(StartLocation.class);
        super.init();
    }

    @Override
    public String getDescription(NPC npc) {
        return null;
    }
}

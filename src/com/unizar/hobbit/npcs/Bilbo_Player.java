package com.unizar.hobbit.npcs;

import com.unizar.game.elements.Player;
import com.unizar.hobbit.rooms.StartLocation;

public class Bilbo_Player extends Player {

    public Bilbo_Player() {
        super("Bilbo");
        weight = 25;
    }

    @Override
    public void init() {
        location = game.findElementByClassName(StartLocation.class);
        super.init();
    }

}

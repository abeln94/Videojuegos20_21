package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class GoblinDoor extends Item {
    public GoblinDoor() {
        super("la puerta goblinera");
        openable = OPENABLE.LOCKED; // can not be unlocked
    }
}

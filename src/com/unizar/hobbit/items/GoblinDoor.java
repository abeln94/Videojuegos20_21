package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class GoblinDoor extends Item {
    public GoblinDoor() {
        super("la puerta goblinera");
    }

    @Override
    public void init() {
        opened = false;
        super.init();
    }
}

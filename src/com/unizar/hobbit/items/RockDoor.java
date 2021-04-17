package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class RockDoor extends Item {
    public RockDoor() {
        super("la pesada puerta de piedra");
    }

    @Override
    public void init() {
        opened = false;
        super.init();
    }
}
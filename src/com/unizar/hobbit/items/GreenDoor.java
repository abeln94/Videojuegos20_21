package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class GreenDoor extends Item {
    public GreenDoor() {
        super("la puerta verde redonda");
    }

    @Override
    public void init() {
        opened = false;
        super.init();
    }
}

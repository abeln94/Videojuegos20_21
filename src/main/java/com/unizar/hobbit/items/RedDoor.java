package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class RedDoor extends Item {
    public RedDoor() {
        super("la puerta roja");
    }

    @Override
    public void init() {
        openable = OPENABLE.CLOSED;
        lockedWith = game.findElementByClassName(RedKey.class);
        super.init();
    }
}

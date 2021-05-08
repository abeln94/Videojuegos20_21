package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class SideDoorLonelyMountain extends Item {
    public SideDoorLonelyMountain() {
        super("la puerta lateral de la Montaña Solitaria");
    }

    @Override
    public void init() {
        openable = OPENABLE.LOCKED;
        lockedWith = game.findElementByClassName(SmallKey.class);
        super.init();
    }
}

package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class SideDoorLonelyMountain extends Item {
    public SideDoorLonelyMountain() {
        super("Tras un fuerte crujido aparece un gran agujero en la pared. Te encuentras ante la puerta lateral de la Montaña Solitaria");
    }

    @Override
    public void init() {
        openable = OPENABLE.CLOSED;
        lockedWith = game.findElementByClassName(SmallKey.class);
        super.init();
    }
}

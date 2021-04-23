package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class GoblinGate extends Item {
    public GoblinGate() {
        super("la puerta de entrada a la goblinera");
        openable = OPENABLE.CLOSED;
    }
}

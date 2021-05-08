package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class GoblinWindow extends Item {
    public GoblinWindow() {
        super("la ventana goblinera");
    }

    @Override
    public void init() {
        openable = OPENABLE.CLOSED;
        super.init();
    }
}

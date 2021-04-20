package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class Window extends Item {
    public Window() {
        super("la ventana goblinera");
    }

    @Override
    public void init() {
        openable = OPENABLE.OPENED;
        super.init();
    }
}

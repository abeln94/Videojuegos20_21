package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class StrongPortcullis extends Item {
    public StrongPortcullis() {
        super("unas fuertes rejas que separan el rio del lago");
    }

    @Override
    public void init() {
        openable = OPENABLE.CLOSED;
        super.init();
    }
}

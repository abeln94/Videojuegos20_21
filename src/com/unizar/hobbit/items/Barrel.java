package com.unizar.hobbit.items;

import com.unizar.game.commands.Word;
import com.unizar.game.elements.Item;

public class Barrel extends Item {
    public Barrel() {
        super("Un tonel");
        weight = 10;
    }

    @Override
    public void init() {
        openable = OPENABLE.CLOSED;
        super.init();
    }
}

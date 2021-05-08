package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class StartChest extends Item {
    public StartChest() {
        super("el cofre de madera");
    }

    @Override
    public void init() {
        openable = OPENABLE.CLOSED;
        super.init();
    }
}

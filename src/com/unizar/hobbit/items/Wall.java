package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class Wall extends Item {
    public Wall() {
        super("una pared.");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Cupboard.class));
        super.init();
    }
}

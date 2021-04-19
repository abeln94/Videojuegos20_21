package com.unizar.hobbit.items;

import com.unizar.game.commands.Result;
import com.unizar.game.elements.Item;

public class HeavyCurtain extends Item {
    public HeavyCurtain() {
        super("una pesada cortina.");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Wall.class));
        super.init();
    }
}

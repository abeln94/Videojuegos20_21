package com.unizar.hobbit.items;

import com.unizar.game.elements.Element;
import com.unizar.game.elements.Item;

public class Cupboard extends Item {
    public Cupboard() {
        super("un gran armario");
    }

    @Override
    public void init() {
        openable = OPENABLE.CLOSED;
        elements.add(game.findElementByClassName(Apples.class));
        super.init();
    }

    @Override
    public String getDescription() {
        return "Una pesada cortina. Tras ella una pared. En la pared hay " + super.getDescription();
    }
}

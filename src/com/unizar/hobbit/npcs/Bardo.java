package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Bow;
import com.unizar.hobbit.items.Map;

public class Bardo extends NPC {

    public Bardo() {
        super("Bardo, el Bardo");
        weight = 50;
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Bow.class));
        super.init();
    }
}

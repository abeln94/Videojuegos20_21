package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Bow;

import java.util.ArrayList;

public class Bardo extends NPC {

    public Bardo() {
        super("Bardo, el Bardo");
        weight = 500;
        id = 1;
        lastAttackedBy = null;
        puedeDormir = false;
        lugares = new ArrayList<>();
        dormido = false;
        orden = null;
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Bow.class));
        super.init();
    }

    @Override
    public void act() {
        super.act();
    }
}

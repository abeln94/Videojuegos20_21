package com.unizar.hobbit.items;

import com.unizar.game.elements.Element;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.NPC;

public class Cupboard extends Item {
    public Cupboard() {
        super("un gran armario");
        openable = OPENABLE.CLOSED;
    }

    @Override
    public String getDescription(NPC npc) {
        return "Una pesada cortina. Tras ella una pared. En la pared hay " + super.getDescription(npc);
    }

    @Override
    public void act() {
        final Element food = game.findElementByClassName(Food.CupboardFood.class);
        if (food.getLocation() == null) {
            food.moveTo(this);
        }
    }
}

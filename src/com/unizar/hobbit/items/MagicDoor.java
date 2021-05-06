package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;
import com.unizar.game.elements.NPC;

public class MagicDoor extends Item {
    public MagicDoor() {
        super("la puerta mágica");
    }

    @Override
    public void init() {
        openable = OPENABLE.CLOSED;
        super.init();
    }

    @Override
    public String examine(NPC npc) {
        return "La puerta mágica te advierte de que se acercan los elfos.";
    }
}

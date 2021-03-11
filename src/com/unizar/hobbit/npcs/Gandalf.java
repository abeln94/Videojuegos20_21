package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Map;

public class Gandalf extends NPC {
    public Gandalf() {
        super("Gandalf");
        elements.add(Map.class);
    }

}

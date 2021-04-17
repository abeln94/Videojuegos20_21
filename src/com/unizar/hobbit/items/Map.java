package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.npcs.Elrond;

public class Map extends Item {

    public static final String ELROND_REPLY = "%elrond_reply%";

    public Map() {
        super("el mapa peculiar");
        weight = 1;
    }

    @Override
    public String examine(NPC npc) {
        if (npc instanceof Elrond) return ELROND_REPLY;
        return "Parece que contiene símbolos, pero no sabes leerlos.";
    }
}

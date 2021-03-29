package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;
import com.unizar.game.elements.NPC;

public class Map extends Item {

    public Map() {
        super("el mapa peculiar");
    }

    @Override
    public String examine(NPC npc) {
        return "Parece que contiene símbolos, pero no sabes leerlos.";
    }
}

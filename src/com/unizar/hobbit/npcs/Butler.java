package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.RedKey;

public class Butler extends NPC {

    private boolean playerSaw = false;

    public Butler() {
        super("El mayordomo");
        weight = 50;
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(RedKey.class));
        super.init();
    }

    //TODO: bebe vino
    //TODO: esperar a que duerma y quitarle la llave
    //TODO:
}

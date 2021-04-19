package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Result;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.rooms.GoblinDungeon;

public class Gollum extends NPC {

    public Gollum() {
        super("Gollum");
        weight = 50;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void act() {
        //TODO: Preguntar por el anillo, atacar y las acciones que se quieran
        super.act();
    }
}

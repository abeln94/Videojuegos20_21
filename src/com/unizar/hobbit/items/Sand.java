package com.unizar.hobbit.items;

import com.unizar.Utils;
import com.unizar.game.commands.Result;
import com.unizar.game.elements.Item;
import com.unizar.hobbit.npcs.Thorin;
import com.unizar.hobbit.rooms.GoblinDungeon;

public class Sand extends Item {
    public Sand() {
        super("arena");
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void act() {
        Result result;
        //si DIG se añade la trap door
        super.act();
    }
}

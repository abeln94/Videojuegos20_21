package com.unizar.hobbit.items;

import com.unizar.game.commands.Result;
import com.unizar.game.elements.Item;

public class TrapDoor extends Item {
    public TrapDoor() {
        super("una trampilla");
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void act() {
        Result result;
        //si SMASH se añade la small curious key
        super.act();
    }
}

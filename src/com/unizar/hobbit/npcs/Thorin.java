package com.unizar.hobbit.npcs;

import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.rooms.StartLocation;

public class Thorin extends NPC {
    public Thorin() {
        super("Thorin");
    }

    @Override
    public void init() {
        location = game.findElementByClassName(StartLocation.class);
        super.init();
    }

    @Override
    public void act() {
        Result result;

        // try following the player
        result = game.engine.execute(this, Command.act(Word.Action.FOLLOW, game.getPlayer()));
        if (result.done) {
            onHear(result.output);
            return;
        }

        // sing about gold

        // do default
        super.act();
    }
}

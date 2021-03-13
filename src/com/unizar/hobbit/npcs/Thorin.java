package com.unizar.hobbit.npcs;

import com.unizar.Utils;
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
        global = true;
        location = game.findElementByClassName(StartLocation.class);
        super.init();
    }

    @Override
    public void act() {
        Result result;

        // try following the player
        result = game.engine.applyCommand(this, Command.act(Word.Action.FOLLOW, game.getPlayer()));
        if (result.done) {
            onHear(result.output);
            return;
        }

        // try going in a random direction
        result = game.engine.applyCommand(this, Command.go(Utils.pickRandom(Word.Direction.values())));
        if (result.done) {
            onHear(result.output);
            return;
        }

        // do default
        super.act();
    }
}

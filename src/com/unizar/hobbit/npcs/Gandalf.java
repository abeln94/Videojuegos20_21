package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Map;
import com.unizar.hobbit.rooms.StartLocation;

public class Gandalf extends NPC {
    public Gandalf() {
        super("Gandalf");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Map.class));
        location = game.findElementByClassName(StartLocation.class);
        super.init();
    }

    @Override
    public void act() {
        Result result;

        // try going in a random direction
        result = game.engine.execute(this, Command.go(Utils.pickRandom(Word.Direction.values())));
        if (result.done) {
            onHear(result.output);
            return;
        }

        // do default
        super.act();
    }
}

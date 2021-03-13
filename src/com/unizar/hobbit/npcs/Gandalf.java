package com.unizar.hobbit.npcs;

import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Map;

import java.util.Random;

public class Gandalf extends NPC {
    public Gandalf() {
        super("Gandalf");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Map.class));
        super.init();
    }

    @Override
    public void act() {
        Random random = new Random();

        // try going in a random direction
        Word.Direction dir = Word.Direction.values()[random.nextInt(Word.Direction.values().length)];
        Result result = game.engine.applyCommand(this, Command.go(dir));
        if (result.done) {
            onHear(result.output);
            return;
        }

        // do default
        super.act();
    }
}

package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.rooms.StartLocation;

public class Thorin extends NPC {

    private int tiredness = 0;

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
            hear(result.output);
            tiredness = 0;
            return;
        }
        tiredness++;

        // hurry up!
        if (tiredness > 3 && Utils.random.nextBoolean()) {
            location.notifyNPCs(this, this + " dice: Date prisa");
            return;
        }

        // sing about gold
        if (tiredness > 5 && Utils.random.nextBoolean()) {
            location.notifyNPCs(this, this + " se sienta y empieza a cantar sobre oro.");
            tiredness = 0;
            return;
        }

        // do default
        super.act();
    }
}

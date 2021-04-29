package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;

public class Thorin extends NPC {

    private int tiredness = 0;

    public Thorin() {
        super("Thorin");
        weight = 26;
    }

    @Override
    public void act() {
        Result result;

        // return attack
        if (lastAttackedBy != null) {
            result = game.engine.execute(Command.act(Word.Action.KILL, lastAttackedBy).asNPC(this));
            lastAttackedBy = null;
            if (result.done) {
                hear(result.output);
                tiredness = 0;
                return;
            }
        }

        // try following the player
        result = game.engine.execute(Command.act(Word.Action.FOLLOW, game.getPlayer()).asNPC(this));
        if (result.done) {
            hear(result.output);
            tiredness = 0;
            return;
        }
        tiredness++;

        // hurry up!
        if (tiredness > 3 && Utils.random.nextBoolean()) {
            getLocation().notifyNPCs(this, this + " dice: Date prisa");
            return;
        }

        // sing about gold
        if (tiredness > 5 && Utils.random.nextBoolean()) {
            getLocation().notifyNPCs(this, this + " se sienta y empieza a cantar sobre oro.");
            tiredness = 0;
            return;
        }

        // do default
        super.act();
    }
}

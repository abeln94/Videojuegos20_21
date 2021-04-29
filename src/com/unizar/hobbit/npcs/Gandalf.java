package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Engine;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.GreenDoor;
import com.unizar.hobbit.items.Map;

public class Gandalf extends NPC {
    public Gandalf() {
        super("Gandalf");
        weight = 50;
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Map.class));
        super.init();
    }

    @Override
    public void act() {
        Result result;

        // return attack
        if (lastAttackedBy != null) {
            result = Engine.execute(Command.act(Word.Action.KILL, lastAttackedBy).asNPC(this));
            lastAttackedBy = null;
            if (result.done) {
                hear(result.output);
                return;
            }
        }

        // try going in a random direction
        result = Engine.execute(Command.go(Utils.pickRandom(Word.Direction.values())).asNPC(this));
        if (result.done) {
            hear(result.output);
            return;
        }

        // try opening the door
        if (Utils.random.nextBoolean()) {
            result = Engine.execute(Command.act(Word.Action.OPEN, game.findElementByClassName(GreenDoor.class)).asNPC(this));
            if (result.done) {
                hear(result.output);
                return;
            }
        }

        // try giving the map to the player
        if (Utils.random.nextBoolean()) {
            result = Engine.execute(Command.act(Word.Action.GIVE, game.findElementByClassName(Map.class), game.findElementByClassName(Bilbo_Player.class)).asNPC(this));
            if (result.done) {
                hear(result.output);
                return;
            }
        }

        // do default
        super.act();
    }
}

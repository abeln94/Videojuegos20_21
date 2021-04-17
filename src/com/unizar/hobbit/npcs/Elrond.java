package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Food;
import com.unizar.hobbit.items.Map;

public class Elrond extends NPC {

    public Elrond() {
        super("Elrond");
        weight = 50;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void act() {
        Result result;

        if (false) { //TODO: si Bilbo le dice READ MAP
            result = game.engine.execute(this, Command.act(Word.Action.EXAMINE, game.findElementByClassName(Map.class)));
            location.notifyNPCs(this, this + " dice: Ve hacia el este desde Long Lake para llegar a Lake Town");
            if (result.done) {
                hear(result.output);
                return;
            }
        }

        if (false) { //TODO: si Bilbo le dice GIVE ME MAP
            result = game.engine.execute(this, Command.act(Word.Action.GIVE, game.findElementByClassName(Map.class), game.findElementByClassName(Bilbo_Player.class)));
            if (result.done) {
                hear(result.output);
                return;
            }
        }

        if (Utils.random.nextBoolean()) {
            result = game.engine.execute(this, Command.act(Word.Action.GIVE, game.findElementByClassName(Food.class), game.findElementByClassName(Bilbo_Player.class)));
            if (result.done) {
                hear(result.output);
                return;
            }
        }

        super.act();
    }
}
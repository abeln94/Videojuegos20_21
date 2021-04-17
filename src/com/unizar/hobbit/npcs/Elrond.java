package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Food;
import com.unizar.hobbit.items.Map;

public class Elrond extends NPC {

    private boolean playerSaw = false;

    public Elrond() {
        super("Elrond");
        weight = 50;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void hear(String message) {
        if (message.contains(Map.ELROND_REPLY)) {
            // we examined the map, only the player can ask us to do it
            location.notifyNPCs(this, this + " dice: Ve hacia el este desde Long Lake para llegar a Lake Town");
            // TODO: add that link from Long Lake to Lake Town
            return;
        }
        super.hear(message);
    }

    @Override
    public void act() {
        Result result;

        if (location.elements.contains(game.getPlayer())) {
            // the player is there
            if (!playerSaw) {
                // first time
                location.notifyNPCs(this, this + " dice: Hola");
                playerSaw = true;
                return;
            }

            final Food food = game.findElementByClassName(Food.class);
            if (!food.alive && Utils.random.nextBoolean()) {
                // give the player food
                food.alive = true;
                elements.add(food);
                result = game.engine.execute(this, Command.act(Word.Action.GIVE, food, game.getPlayer()));
                if (result.done) {
                    hear(result.output);
                    return;
                }
            }

        }


        super.act();
    }
}
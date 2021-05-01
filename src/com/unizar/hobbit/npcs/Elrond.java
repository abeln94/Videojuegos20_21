package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Element;
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
            getLocation().notifyNPCs(this, this + " dice: Ve hacia el este desde el Gran Lago para llegar a Ciudad del lago");
            return;
        }
        super.hear(message);
    }

    @Override
    public void act() {
        Result result;

        if (getLocation().elements.contains(game.getPlayer())) {
            // the player is there
            if (!playerSaw) {
                // first time
                getLocation().notifyNPCs(this, this + " dice: Hola");
                playerSaw = true;
                return;
            }

            final Element food = game.findElementByClassName(Food.ElrondFood.class);
            if (food.getLocation() == null && Utils.random.nextBoolean()) {
                // give the player food
                food.moveTo(this);
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
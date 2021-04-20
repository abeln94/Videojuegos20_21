package com.unizar.hobbit.npcs;

import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.LargeKey;

public class HideousTroll extends NPC {

    private boolean playerSaw = false;

    public HideousTroll() {
        super("Un Troll horrendo");
        weight = 200;
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(LargeKey.class));
        super.init();
    }

    @Override
    public void act() {
        if (!game.world.night) return; // is a rock

        Result result;

        if (getLocation().elements.contains(game.getPlayer())) {
            // the player is there
            if (!playerSaw) {
                // first time
                getLocation().notifyNPCs(this, this + " dice: ¡¡Mira, mira!! ¿Me los pue hamar?");
                playerSaw = true;
                return;
            } else {
                // not first time, eat player
                result = game.engine.execute(this, Command.act(Word.Action.EAT, game.findElementByClassName(Bilbo_Player.class)));
                if (result.done) {
                    hear(result.output);
                    return;
                }
            }
        }
        super.act();
    }
}

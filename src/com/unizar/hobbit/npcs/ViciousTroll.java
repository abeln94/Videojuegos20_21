package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;

public class ViciousTroll extends NPC {

    private boolean playerSaw = false;

    public ViciousTroll() {
        super("Un Troll pendenciero");
        weight = 200;
    }

    @Override
    public void act() {
        if (!game.world.night) return; // is a rock

        if (getLocation().elements.contains(game.getPlayer())) {
            // the player is there
            if (!playerSaw) {
                // first time
                getLocation().notifyNPCs(this, this + " dice: Puedo proba, pero no dan pa na");
                playerSaw = true;
                return;
            }
        }

        super.act();
    }
}

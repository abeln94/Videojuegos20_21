package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;

public class ViciousTroll extends NPC {

    private boolean firstInteraction = true;

    public ViciousTroll() {
        super("Un Troll pendenciero");
        weight = 200;
    }

    @Override
    public void act() {
        if (!game.world.night) return; // is a rock

        if (location.elements.contains(game.getPlayer())) {
            // the player is there
            if (firstInteraction) {
                // first time
                location.notifyNPCs(this, this + " dice: Puedo proba, pero no dan pa na");
                firstInteraction = false;
                return;
            }
        }

        super.act();
    }
}

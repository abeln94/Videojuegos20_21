package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;
import com.unizar.hobbit.rooms.TrollsClearing;

public class ViciousTroll extends NPC {

    public ViciousTroll() {
        super("Un Troll pendenciero");
        weight = 200;
    }

    @Override
    public void act() {
        if(game.world.time < 10){
            location.notifyNPCs(this, this + " dice: Puedo proba, pero no dan pa na");
            this.FirstInteraction = false;
        }
        super.act();
    }
}

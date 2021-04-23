package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Result;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.rooms.GoblinDungeon;

public class NastyGoblin extends NPC {

    public NastyGoblin() {
        super("Un goblin repugnante");
        weight = 50;
    }

    @Override
    public void act() {
        Result result;
        //se mueven a la ubicacion
        if (getLocation().elements.contains(game.getPlayer()) && Utils.random.nextBoolean()) {
            getLocation().notifyNPCs(this, this + " dice: To de cave");
            game.getPlayer().moveTo(game.findElementByClassName(GoblinDungeon.class));
            game.findElementByClassName(Thorin.class).moveTo(game.findElementByClassName(GoblinDungeon.class));
        }
        super.act();
    }
}

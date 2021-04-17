package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.LargeKey;
import com.unizar.hobbit.rooms.EmptyLand;
import com.unizar.hobbit.rooms.TrollsClearing;

public class HideousTroll extends NPC {

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
        Result result;
        if(game.world.time < 10){
            location.notifyNPCs(this, this + " dice: ¡¡Mira, mira!! ¿Me los pue hamar?");
            this.FirstInteraction = false;
        }

        if (Utils.random.nextBoolean()) {
            result = game.engine.execute(this, Command.act(Word.Action.EAT, game.findElementByClassName(Bilbo_Player.class))); //TODO: lo mata
            if (result.done) {
                hear(result.output);
                return;
            }
        }
        super.act();
    }
}

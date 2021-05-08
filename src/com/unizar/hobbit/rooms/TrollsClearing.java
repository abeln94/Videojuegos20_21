package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.LargeKey;
import com.unizar.hobbit.npcs.HideousTroll;
import com.unizar.hobbit.npcs.ViciousTroll;

public class TrollsClearing extends Location {
    public TrollsClearing() {
        super("el claro de los trolls", "TrollsClearingNight", "CamoAlla"); //noche
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(HideousTroll.class));
        elements.add(game.findElementByClassName(ViciousTroll.class));

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(EmptyLand.class), null));
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(EmptyLand.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(HiddenPath.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(Rivendell.class), null));
        super.init();
    }

    @Override
    public void act() {
        final HideousTroll troll1 = game.findElementByClassName(HideousTroll.class);
        final ViciousTroll troll2 = game.findElementByClassName(ViciousTroll.class);
        final LargeKey key = game.findElementByClassName(LargeKey.class);

        if (game.world.night) {
            // night
            name = "el claro de los trolls";
            image = "TrollsClearingNight";

            // trolls are visible
            elements.add(troll1);
            elements.add(troll2);
            if (elements.contains(key)) key.moveTo(troll1);

        } else {
            // day
            name = "el claro de los trolls con dos trolls de piedra";
            image = "TrollsClearingDay";

            // trolls are stone
            elements.remove(troll1);
            elements.remove(troll2);
            if (troll1.elements.contains(key)) key.moveTo(this);
        }
    }
}

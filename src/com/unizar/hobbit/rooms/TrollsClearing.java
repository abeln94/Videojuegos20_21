package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.npcs.HideousTroll;
import com.unizar.hobbit.npcs.ViciousTroll;

public class TrollsClearing extends Location {
    public TrollsClearing() {
        super("el claro de los trolls", "TrollsClearingNight", "forest"); //noche
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
}

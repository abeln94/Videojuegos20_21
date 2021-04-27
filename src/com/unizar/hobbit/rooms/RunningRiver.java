package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class RunningRiver extends Location {

    public RunningRiver() {
        super("en un río con rápidos", "RunningRiver");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(Waterfall.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(Forest.class), null));
        super.init();
    }
}

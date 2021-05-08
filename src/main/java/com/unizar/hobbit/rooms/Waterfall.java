package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class Waterfall extends Location {

    public Waterfall() {
        super("en las cascadas.", "Waterfall", "water");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(RunningRiver.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(FastRiver.class), null));
        super.init();
    }
}

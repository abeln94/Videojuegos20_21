package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class FastRiver extends Location {

    public FastRiver() {
        super("en el río rápido.", "Forestriver");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(FastRiver.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(FastRiver.class), null));
        super.init();
    }
}

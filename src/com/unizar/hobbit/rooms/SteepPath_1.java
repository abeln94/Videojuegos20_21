package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class SteepPath_1 extends Location {

    public SteepPath_1() {
        super("en un empinado camino", "SteepPath", "iceCavern");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.DOWN, Utils.Pair.of(game.findElementByClassName(SteepPath_2.class),null));
        super.init();
    }
}

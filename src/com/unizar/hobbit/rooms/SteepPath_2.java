package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class SteepPath_2 extends Location {

    public SteepPath_2() {
        super("en un empinado camino, la niebla es menos densa", "SteepPath", "iceCavern");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.DOWN, Utils.Pair.of(game.findElementByClassName(SteepPath_3.class),null));
        super.init();
    }
}

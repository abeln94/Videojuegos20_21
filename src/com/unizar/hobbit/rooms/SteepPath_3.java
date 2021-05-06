package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class SteepPath_3 extends Location {

    public SteepPath_3() {
        super("en un empinado camino, empiezas a oir agua", "SteepPath", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.DOWN, Utils.Pair.of(game.findElementByClassName(MistyValley_1.class),null));
        super.init();
    }
}

package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class NarrowPath_2 extends Location {

    public NarrowPath_2() {
        super("en un estrecho camino lleno de niebla", "DangerousPath", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(NarrowPath_1.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(NarrowPath_3.class),null));
        super.init();
    }
}

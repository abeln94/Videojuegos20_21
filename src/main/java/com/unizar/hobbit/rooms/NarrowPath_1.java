package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class NarrowPath_1 extends Location {

    public NarrowPath_1() {
        super("en un estrecho camino lleno de niebla", "DangerousPath", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(NarrowPath_2.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(NarrowPath_1.class),null));
        super.init();
    }
}

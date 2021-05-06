package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class NarrowPath_3 extends Location {

    public NarrowPath_3() {
        super("en un estrecho camino con obstaculos", "DangerousPath", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTHWEST, Utils.Pair.of(game.findElementByClassName(NarrowPath_4.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(NarrowPath_2.class),null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(NarrowPath_7.class),null));
        super.init();
    }
}

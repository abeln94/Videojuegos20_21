package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class NarrowPath_4 extends Location {

    public NarrowPath_4() {
        super("en un estrecho camino con obstaculos", "DangerousPath", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(DeadlyPath.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(NarrowPath_3.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(NarrowPath_5.class),null));
        super.init();
    }
}

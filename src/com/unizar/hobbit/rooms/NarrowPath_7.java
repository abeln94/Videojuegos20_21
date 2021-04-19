package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class NarrowPath_7 extends Location {

    public NarrowPath_7() {
        super("en un estrecho camino con obstaculos", "OtherNarrowPath");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(NarrowPath_3.class), null));
        exits.put(Word.Direction.NORTHEAST, Utils.Pair.of(game.findElementByClassName(DeadlyPath.class),null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(DangerousPath.class),null));
        super.init();
    }
}

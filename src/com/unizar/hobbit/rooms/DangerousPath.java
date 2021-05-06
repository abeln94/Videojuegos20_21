package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DangerousPath extends Location {

    public DangerousPath() {
        super("en un sinuoso y peligroso camino en las montañas brumosas", "DangerousPath", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(Rivendell.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DimValley.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(DeadlyPath.class),null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(NarrowPath_1.class),null));
        super.init();
    }
}

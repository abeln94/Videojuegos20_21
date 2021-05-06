package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class NarrowDangerousPath extends Location {

    public NarrowDangerousPath() {
        super("en un camino estrecho y peligroso", "NarrowDangerousPath", "wind");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(DimValley.class), null));
        //exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(.class), null)); //TODO:
        super.init();
    }
}

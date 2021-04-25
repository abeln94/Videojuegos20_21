package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.Cupboard;

public class NarrowDangerousPath extends Location {

    public NarrowDangerousPath() {
        super("en un camino estrecho y peligroso", "NarrowDangerousPath");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(DimValley.class), null));
        //exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(.class), null)); //TODO:
        super.init();
    }
}

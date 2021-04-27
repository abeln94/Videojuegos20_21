package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DimValley extends Location {

    public DimValley() {
        super("en un estrecho camino al borde de un barranco oscuro y profundo", "DimValley");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(NarrowDangerousPath.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(DryCave.class),null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(DangerousPath.class), null));
        super.init();
    }
}

package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DryCave extends Location {

    public DryCave() {
        super("en un una larga y seca cueva, bastante acogedora. Debajo tiene una pequeña grieta", "DryCave");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(DimValley.class), null));
        super.init();
    }
}
package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class StrongRiver extends Location {

    public StrongRiver() {
        super("en un río con corrientes muy fuertes", "Forestriver", "water");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.UP, Utils.Pair.of(game.findElementByClassName(BleakBarrenLand.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(LongLake.class), null));
        super.init();
    }
}

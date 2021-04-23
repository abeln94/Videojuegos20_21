package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DarkWidingPassage_6 extends Location {

    public DarkWidingPassage_6() {
        super("en un pasaje oscuro y cerrado", "DarkWidingPassage");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_7.class), null));
        exits.put(Word.Direction.NORTHWEST, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        super.init();
    }
}

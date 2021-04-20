package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DarkWidingPassage_2 extends Location {

    public DarkWidingPassage_2() {
        super("en un pasaje oscuro y cerrado", "DarkWidingPassage");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_4.class), null));
        super.init();
    }
}

package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DarkWidingPassage_3 extends Location {

    public DarkWidingPassage_3() {
        super("en un pasaje oscuro y cerrado", "DarkWidingPassage", "CourtJesters");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_4.class), null));
        super.init();
    }
}

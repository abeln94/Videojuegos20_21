package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DarkWidingPassage_4 extends Location {

    public DarkWidingPassage_4() {
        super("en un pasaje oscuro y cerrado", "DarkWidingPassage", "CourtJesters");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_2.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_5.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        super.init();
    }
}

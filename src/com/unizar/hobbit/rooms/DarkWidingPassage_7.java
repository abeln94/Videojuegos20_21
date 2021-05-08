package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DarkWidingPassage_7 extends Location {

    public DarkWidingPassage_7() {
        super("en un pasaje ancho y oscuro", "DarkWidingPassage", "CourtJesters");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_6.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_6.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_8.class), null));
        super.init();
    }
}

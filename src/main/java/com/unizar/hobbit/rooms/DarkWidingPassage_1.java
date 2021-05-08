package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DarkWidingPassage_1 extends Location {

    public DarkWidingPassage_1() {
        super("en un pasaje ancho y oscuro", "DarkWidingPassage", "cave");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_2.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_3.class), null));
        super.init();
    }
}

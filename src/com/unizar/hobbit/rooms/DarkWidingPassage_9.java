package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DarkWidingPassage_9 extends Location {

    public DarkWidingPassage_9() {
        super("en un pasaje ancho y oscuro", "DarkWidingPassage", "cave");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_8.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(GoblinsGate.class), null));
        super.init();
    }
}

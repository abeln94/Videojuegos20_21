package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DarkWidingPassage_8 extends Location {

    public DarkWidingPassage_8() {
        super("en un pasaje ancho y oscuro", "DarkWidingPassage");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTHWEST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_9.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        super.init();
    }
}

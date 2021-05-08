package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class WestBlackRiver extends Location {

    public WestBlackRiver() {
        super("en la orilla oesta del río Negro", "WestBlackRiver", "water");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(EastBlackRiver.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(BewitchedPlace.class), null));
        super.init();
    }
}

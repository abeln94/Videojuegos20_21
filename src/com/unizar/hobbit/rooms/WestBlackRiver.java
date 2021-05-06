package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.Gold;

public class WestBlackRiver extends Location {

    public WestBlackRiver() {
        super("en la orilla oesta del río Negro", "WestBlackRiver", "water");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(BewitchedPlace.class), null));
        elements.add(game.findElementByClassName(Gold.class));
        super.init();
    }
}

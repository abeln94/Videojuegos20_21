package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class BewitchedPlace extends Location {

    public BewitchedPlace() {
        super("en un lugar tenebroso, incluso parece embrujado", "BewitchedPlace");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(WestBlackRiver.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(ForestGate.class), null));
        super.init();
    }
}

package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class RuinsTownDale extends Location {

    public RuinsTownDale() {
        super(" en las ruinas del pueblo de Dale", "RuinsTownDale", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(FrontGate.class), null));
        exits.put(Word.Direction.NORTHWEST, Utils.Pair.of(game.findElementByClassName(Ravenhill.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(BleakBarrenLand.class), null));
        super.init();
    }
}

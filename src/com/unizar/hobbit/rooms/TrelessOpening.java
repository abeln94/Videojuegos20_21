package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class TrelessOpening extends Location {

    public TrelessOpening() {
        super("en un claro del bosque", "TrelessOpening");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(BeornsHouse.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(OutsideGoblinsGate.class), null));
        super.init();
    }
}
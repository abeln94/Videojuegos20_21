package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class ForestRoad_2 extends Location {

    public ForestRoad_2() {
        super("en un camino forestal", "ForestRoad_2", "forest");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(ForestRoad_1.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(ForestGate.class), null));
        super.init();
    }
}

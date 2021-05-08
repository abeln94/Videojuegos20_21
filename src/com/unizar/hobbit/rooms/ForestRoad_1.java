package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class ForestRoad_1 extends Location {

    public ForestRoad_1() {
        super("en un camino forestal lleno de piedras", "ForestRoad_1", "DangerForest");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(ForestRoad_2.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(Forest.class), null));
        super.init();
    }
}

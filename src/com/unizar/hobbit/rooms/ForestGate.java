package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class ForestGate extends Location {

    public ForestGate() {
        super("en la entrada del bosque", "ForestRoad_2", "DangerForest");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(BeornsHouse.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(ForestRoad_2.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(BewitchedPlace.class), null));
        super.init();
    }
}

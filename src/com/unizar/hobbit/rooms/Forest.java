package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class Forest extends Location {

    public Forest() {
        super("en un bosque", "Forest", "DangerForest");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(ForestRoad_1.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(RunningRiver.class), null));
        super.init();
    }
}

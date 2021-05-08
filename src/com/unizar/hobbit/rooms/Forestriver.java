package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.LargeTrapDoor;

public class Forestriver extends Location {

    public Forestriver() {
        super(" en el río que atraviesa el bosque. Vas en uno de los toneles ", "Forestriver", "water");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.UP, Utils.Pair.of(game.findElementByClassName(CellarOfWine.class), game.findElementByClassName(LargeTrapDoor.class)));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(FastRiver.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(LongLake.class), null));
        super.init();
    }
}

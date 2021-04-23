package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GreenDoor;

public class StartLocation extends Location {

    public StartLocation() {
        super("una sala alargada y cálida", "StartLocation");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(EmptyLand.class), game.findElementByClassName(GreenDoor.class)));
        super.init();
    }
}

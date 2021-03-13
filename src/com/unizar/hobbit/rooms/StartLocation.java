package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GreenDoor;
import com.unizar.hobbit.items.StartChest;

public class StartLocation extends Location {

    public StartLocation() {
        super("una sala alargada confortable", "2");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(StartChest.class));
        exits.put(Word.Direction.EAST, new Utils.Pair<>(game.findElementByClassName(EastLocation.class), game.findElementByClassName(GreenDoor.class)));
        super.init();
    }
}

package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.SideDoorLonelyMountain;

public class Sidedoor extends Location {

    public Sidedoor() {
        super("en una pequeña bahía empinada, tranquila y silenciosa, con un acantilado que sobresale.", "Sidedoor", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(EmptyPlace.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(Ravenhill.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(SmoothPassage.class), game.findElementByClassName(SideDoorLonelyMountain.class)));
        super.init();
    }
}

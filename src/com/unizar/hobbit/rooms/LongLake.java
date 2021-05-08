package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.StrongPortcullis;

public class LongLake extends Location {

    public LongLake() {
        super(" en el Gran Lago", "LongLake", "shores");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTHWEST, Utils.Pair.of(game.findElementByClassName(StrongRiver.class), game.findElementByClassName(StrongPortcullis.class)));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(StrongRiver.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(LakeTown.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(Waterfall.class), null));
        super.init();
    }
}

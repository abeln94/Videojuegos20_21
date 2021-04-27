package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.Arrow;
import com.unizar.hobbit.npcs.Bardo;

public class LakeTown extends Location {

    public LakeTown() {
        super(" en la ciudad de madera en medio del Gran Lago. Ciudad del lago. ", "LakeTown");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Bardo.class));
        elements.add(game.findElementByClassName(Arrow.class));

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(LongLake.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(LongLake.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(LongLake.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(LongLake.class), null));
        super.init();
    }
}

package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DeadlyPath extends Location {

    public DeadlyPath() {
        super("en un estrecho camino, no puedes ver apenas por la niebla", "DangerousPath");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(DeadlyPath.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DeadlyPath.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(DeadlyPath.class),null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(DeadlyPath.class),null));
        super.init();
    }
}

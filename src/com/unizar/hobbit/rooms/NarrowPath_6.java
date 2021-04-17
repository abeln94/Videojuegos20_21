package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class NarrowPath_6 extends Location {

    public NarrowPath_6() {
        super("en un estrecho camino, ya puedes ver el final", "DangerousPath");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTHWEST, Utils.Pair.of(game.findElementByClassName(NarrowPath_5.class), null));
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(DeadlyPath.class),null));
        exits.put(Word.Direction.DOWN, Utils.Pair.of(game.findElementByClassName(SteepPath_1.class),null));
        super.init();
    }
}

package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class NarrowPath_5 extends Location {

    public NarrowPath_5() {
        super("en un estrecho camino, empiezas a divisar el final de la montaña", "DangerousPath");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(NarrowPath_4.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(NarrowPath_6.class),null));
        super.init();
    }
}

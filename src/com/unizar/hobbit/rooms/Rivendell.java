package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class Rivendell extends Location {

    public Rivendell() {
        super("en Rivendell", "Rivendell");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DangerousPath.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(EmptyLand.class), null));
        super.init();
    }
}
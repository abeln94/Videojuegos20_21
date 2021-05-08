package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.npcs.Elrond;

public class Rivendell extends Location {

    public Rivendell() {
        super("en Rivendell", "Rivendell", "rivendell");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Elrond.class));

        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DangerousPath.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(EmptyLand.class), null));
        super.init();
    }
}
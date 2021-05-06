package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class DeadlyPassage extends Location {

    public DeadlyPassage() {
        super("en un pasaje laberintico y oscuro", "DarkWidingPassage", "cave");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        super.init();
    }
}

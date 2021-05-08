package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GoblinGate;

public class GoblinsGate extends Location {

    public GoblinsGate() {
        super("en la entrada a la goblinera", "GoblinsGate", "CourtJesters");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_4.class), null));
        exits.put(Word.Direction.NORTHWEST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_3.class), null));
        exits.put(Word.Direction.NORTHEAST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_5.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_6.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(DeadlyPassage.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_7.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(DarkWidingPassage_8.class), null));
        exits.put(Word.Direction.UP, Utils.Pair.of(game.findElementByClassName(OutsideGoblinsGate.class), game.findElementByClassName(GoblinGate.class)));
        super.init();
    }
}

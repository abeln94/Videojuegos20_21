package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GoblinGate;

public class OutsideGoblinsGate extends Location {

    public OutsideGoblinsGate() {
        super("fuera de la goblinera", "OutsideGoblinsGate", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(TrelessOpening.class), null));
        exits.put(Word.Direction.DOWN, Utils.Pair.of(game.findElementByClassName(GoblinsGate.class), game.findElementByClassName(GoblinGate.class)));
        super.init();
    }
}

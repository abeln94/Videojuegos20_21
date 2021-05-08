package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class FrontGate extends Location {

    public FrontGate() {
        super("en la puerta principal de la Montaña Solitaria.", "FrontGate", "ommmm");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(LonelyMountain.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(RuinsTownDale.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(Ravenhill.class), null));
        super.init();
    }
}

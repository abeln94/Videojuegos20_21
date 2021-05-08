package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class LonelyMountain extends Location {

    public LonelyMountain() {
        super("en la Montaña Solitaria.", "LonelyMountain", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.DOWN, Utils.Pair.of(game.findElementByClassName(EmptyPlace.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(Sidedoor.class), null));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(FrontGate.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(Ravenhill.class), null));
        super.init();
    }
}

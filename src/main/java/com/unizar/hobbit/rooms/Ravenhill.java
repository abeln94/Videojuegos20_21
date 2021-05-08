package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class Ravenhill extends Location {

    public Ravenhill() {
        super(" en el lado oeste de La Colina de los Cuervos. ", "Ravenhill", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(Sidedoor.class), null));
        exits.put(Word.Direction.SOUTHEAST, Utils.Pair.of(game.findElementByClassName(RuinsTownDale.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(FrontGate.class), null));
        super.init();
    }
}

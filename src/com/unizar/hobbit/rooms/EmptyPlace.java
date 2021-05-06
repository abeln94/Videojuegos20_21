package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class EmptyPlace extends Location {

    public EmptyPlace() {
        super("en un lugar vacío.", "EmptyPlace", "calm");
    }

    @Override
    public void init() {
        //exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(.class), null)); //TODO:
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(Sidedoor.class), null));
        exits.put(Word.Direction.UP, Utils.Pair.of(game.findElementByClassName(LonelyMountain.class), null));
        super.init();
    }
}

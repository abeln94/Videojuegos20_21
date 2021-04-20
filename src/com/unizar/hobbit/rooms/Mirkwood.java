package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class Mirkwood extends Location {

    public Mirkwood() {
        super("en la entrada de Mirkwood", "Mirkwood");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(BewitchedPlace.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(BeornsHouse.class), null));
        //exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(.class), null)); //TODO
        super.init();
    }
}

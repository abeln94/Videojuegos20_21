package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class EastBlackRiver extends Location {

    public EastBlackRiver() {
        super("en la orilla esta del río Negro", "EastBlackRiver", "water");
    }

    @Override
    public void init() {
        //TODO: añadir temas del bote
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(GreenForest.class), null));
        super.init();
    }
}


package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.MagicDoor;
import com.unizar.hobbit.items.RedDoor;

public class Elvenkings extends Location {

    public Elvenkings() {
        super(" en los grandes salones del reino de los elfos.", "Elvenkings");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(ElvishClearing.class), game.findElementByClassName(MagicDoor.class)));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(RedDoorRoom.class), game.findElementByClassName(RedDoor.class)));
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(CellarOfWine.class), null));
        super.init();
    }
}

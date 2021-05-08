package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.RockDoor;

public class HiddenPath extends Location {

    public HiddenPath() {
        super("un camino oculto con pisadas de troll", "HiddenPath", "CamoAlla");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(TrollsClearing.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(TrollsCave.class), game.findElementByClassName(RockDoor.class)));
        super.init();
    }
}

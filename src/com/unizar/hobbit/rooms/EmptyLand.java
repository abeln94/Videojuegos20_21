package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GreenDoor;

public class EmptyLand extends Location {

    public EmptyLand() {
        super("una tierra sombría y yerma con lúgubres colinas en la lejanía", "EmptyLand");
        music = "prelude";
    }

    @Override
    public void init() {
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(StartLocation.class), game.findElementByClassName(GreenDoor.class)));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(TrollsClearing.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(TrollsClearing.class), null));
        exits.put(Word.Direction.NORTHEAST, Utils.Pair.of(game.findElementByClassName(HiddenPath.class), null));
        super.init();
    }
}

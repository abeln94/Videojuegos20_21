package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.RockDoor;
import com.unizar.hobbit.items.Rope;
import com.unizar.hobbit.items.Sword;

public class TrollsCave extends Location {

    public TrollsCave() {
        super("la cueva de los trolls", "TrollsCave", "cave");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Sword.class));
        elements.add(game.findElementByClassName(Rope.class));

        exits.put(Word.Direction.SOUTH, Utils.Pair.of(game.findElementByClassName(HiddenPath.class), game.findElementByClassName(RockDoor.class)));
        super.init();
    }
}
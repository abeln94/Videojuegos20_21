package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GoblinDoor;
import com.unizar.hobbit.items.GoblinWindow;

public class GoblinDungeon extends Location {

    public GoblinDungeon() {
        super("en la mazmorra de los goblins. Te han secuestrado", "GoblinDungeon");
    }

    @Override
    public void init() {

        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(DimValley.class), game.findElementByClassName(GoblinDoor.class)));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(DimValley.class), game.findElementByClassName(GoblinWindow.class)));
        super.init();
    }
}

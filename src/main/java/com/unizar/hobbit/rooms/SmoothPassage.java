package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.SideDoorLonelyMountain;

public class SmoothPassage extends Location {

    public SmoothPassage() {
        super("en una pasaje recto y liso.", "SmoothPassage", "chimney");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DragonsHalls.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(Sidedoor.class), game.findElementByClassName(SideDoorLonelyMountain.class)));
        super.init();
    }
}

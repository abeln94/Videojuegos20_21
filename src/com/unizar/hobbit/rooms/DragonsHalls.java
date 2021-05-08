package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.SideDoorLonelyMountain;
import com.unizar.hobbit.items.TheValuableTreasure;

public class DragonsHalls extends Location {

    public DragonsHalls() {
        super("en la Cueva de Smaug.", "DragonsHalls", "ommmm");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(TheValuableTreasure.class));

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(SmoothPassage.class), null));
        exits.put(Word.Direction.UP, Utils.Pair.of(game.findElementByClassName(LonelyMountain.class), game.findElementByClassName(SideDoorLonelyMountain.class)));
        super.init();
    }
}

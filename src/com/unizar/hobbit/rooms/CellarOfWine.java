package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.Barrel;
import com.unizar.hobbit.items.LargeTrapDoor;
import com.unizar.hobbit.items.RedDoor;
import com.unizar.hobbit.npcs.Butler;

public class CellarOfWine extends Location {

    public CellarOfWine() {
        super(" en la bodega donde el rey guarda sus toneles de vino.", "CellarOfWine");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Barrel.class));
        elements.add(game.findElementByClassName(Butler.class));

        exits.put(Word.Direction.NORTHEAST, Utils.Pair.of(game.findElementByClassName(RedDoorRoom.class), game.findElementByClassName(RedDoor.class)));
        exits.put(Word.Direction.DOWN, Utils.Pair.of(game.findElementByClassName(Forestriver.class), game.findElementByClassName(LargeTrapDoor.class))); //TODO: solo se puede atravesar yendo en un barril
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(Elvenkings.class), null));
        super.init();
    }
}

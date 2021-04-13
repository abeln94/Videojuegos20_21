package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.Gold;
import com.unizar.hobbit.items.GreenDoor;

public class EastLocation extends Location {

    public EastLocation() {
        super("un terreno vacío y sombrío con colinas lúgubres a lo lejos", "3");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Gold.class));

        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(StartLocation.class), game.findElementByClassName(GreenDoor.class)));
        super.init();
    }
}

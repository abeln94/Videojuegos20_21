package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GreenDoor;

public class EastLocation extends Location {

    public EastLocation() {
        super("un terreno vacío y sombrío con colinas lúgubres a lo lejos", "3");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.WEST, new Utils.Pair<>(game.findElementByClassName(StartLocation.class), game.findElementByClassName(GreenDoor.class)));
        super.init();
    }
}

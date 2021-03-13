package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GreenDoor;
import com.unizar.hobbit.items.StartChest;
import com.unizar.hobbit.npcs.Bilbo_Player;
import com.unizar.hobbit.npcs.Gandalf;

public class StartLocation extends Location {

    public StartLocation() {
        super("una sala alargada confortable", "2");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(StartChest.class));
        elements.add(game.findElementByClassName(Bilbo_Player.class));
        elements.add(game.findElementByClassName(Gandalf.class));
        exits.put(Word.Direction.EAST, new Utils.Pair<>(game.findElementByClassName(EastLocation.class), game.findElementByClassName(GreenDoor.class)));
        super.init();
    }
}

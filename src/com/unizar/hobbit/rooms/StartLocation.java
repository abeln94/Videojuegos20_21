package com.unizar.hobbit.rooms;

import com.unizar.game.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GreenDoor;
import com.unizar.hobbit.items.StartChest;
import com.unizar.hobbit.npcs.Bilbo_Player;
import com.unizar.hobbit.npcs.Gandalf;

public class StartLocation extends Location {

    public StartLocation() {
        super("una sala alargada confortable", "2");
        elements.add(StartChest.class);
        elements.add(Bilbo_Player.class);
        elements.add(Gandalf.class);
        exits.put(Word.Direction.EAST, new Utils.Pair<>(EastLocation.class, GreenDoor.class));
    }
}

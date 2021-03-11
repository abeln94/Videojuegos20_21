package com.unizar.hobbit.rooms;

import com.unizar.game.commands.Direction;
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
        exits.put(Direction.EAST, GreenDoor.class);
    }
}

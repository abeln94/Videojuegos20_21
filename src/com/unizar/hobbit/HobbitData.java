package com.unizar.hobbit;

import com.unizar.game.Data;
import com.unizar.hobbit.items.GreenDoor;
import com.unizar.hobbit.items.Map;
import com.unizar.hobbit.items.StartChest;
import com.unizar.hobbit.npcs.Bilbo_Player;
import com.unizar.hobbit.npcs.Gandalf;
import com.unizar.hobbit.rooms.EastLocation;
import com.unizar.hobbit.rooms.StartLocation;

/**
 * This should be better with annotations, but from now lets use it explicitly
 */
public class HobbitData extends Data {

    public HobbitData() {
        // properties
        register(new HobbitProperties());

        // player
        register(new Bilbo_Player());

        // rooms
        register(new StartLocation());
        register(new EastLocation());

        // items
        register(new Map());
        register(new StartChest());
        register(new GreenDoor());

        // npcs
        register(new Gandalf());

    }

}

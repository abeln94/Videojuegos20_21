package com.unizar.hobbit;

import com.unizar.game.Data;
import com.unizar.hobbit.items.Map;
import com.unizar.hobbit.items.StartChest;
import com.unizar.hobbit.npcs.Bilbo_Player;
import com.unizar.hobbit.npcs.Gandalf;
import com.unizar.hobbit.rooms.EastRoom;
import com.unizar.hobbit.rooms.StartRoom;

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
        register(new StartRoom());
        register(new EastRoom());

        // items
        register(new Map());
        register(new StartChest());

        // npcs
        register(new Gandalf());

    }

}

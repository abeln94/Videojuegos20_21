package com.unizar.hobbit;

import com.unizar.game.Room;
import com.unizar.hobbit.rooms.InitialRoom;
import com.unizar.hobbit.rooms.NorthRoom;

import java.util.HashMap;

/**
 * This should be better with annotations, but from now lets use it explicitly
 */
public class Elements {

    //////////// rooms ///////////////
    static public HashMap<String, Room> map = new HashMap<>();

    static {
        map.put("initial", new InitialRoom());
        map.put("north", new NorthRoom());
    }

    static Room getRoom(String name) {
        Room room = map.get(name);
        if (room == null) {
            throw new RuntimeException("Unknown room: " + name);
        }
        return room;
    }

}

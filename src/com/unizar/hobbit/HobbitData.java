package com.unizar.hobbit;

import com.unizar.game.Data;
import com.unizar.hobbit.npcs.Gandalf;
import com.unizar.hobbit.rooms.InitialRoom;
import com.unizar.hobbit.rooms.NorthRoom;
import com.unizar.hobbit.rooms.StartScreen;

/**
 * This should be better with annotations, but from now lets use it explicitly
 */
public class HobbitData extends Data {
    public String getTitle() {
        return "El hobbit";
    }

    public String getImagePath(String label) {
        return "/128k/" + label + ".PNG";
    }

    public HobbitData() {
        // rooms
        register(START_SCREEN, new StartScreen());
        register("initial", new InitialRoom());
        register("north", new NorthRoom());

        // npcs
        register("gandalf", new Gandalf());
    }

}

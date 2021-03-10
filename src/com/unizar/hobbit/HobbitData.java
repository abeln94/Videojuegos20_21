package com.unizar.hobbit;

import com.unizar.game.Data;
import com.unizar.hobbit.npcs.Bilbo_Player;
import com.unizar.hobbit.npcs.Gandalf;
import com.unizar.hobbit.rooms.NorthRoom;
import com.unizar.hobbit.rooms.StartRoom;

/**
 * This should be better with annotations, but from now lets use it explicitly
 */
public class HobbitData extends Data {
    public String getTitle() {
        return "El hobbit";
    }

    @Override
    public int getImageRatio() {
        return 2;
    }

    public String getImagePath(String label) {
        return "/128k/" + label + ".PNG";
    }

    @Override
    public String getFontName() {
        return "/fonts/MorrisRoman-Black.ttf";
    }

    @Override
    public String getStartScreen() {
        return "1_español";
    }

    @Override
    public String getDescription() {
        return "Eren un hobbit, tu objetivo es derrotar al dragon y quedarte su tesoro.";
    }

    public HobbitData() {
        // rooms
        register("initial", new StartRoom());
        register("north", new NorthRoom());

        // player
        register(new Bilbo_Player());

        // npcs
        register("gandalf", new Gandalf());
    }

}

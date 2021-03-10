package com.unizar.hobbit.npcs;

import com.unizar.game.Player;

public class Bilbo_Player extends Player {
    public boolean hasMap = true;

    public Bilbo_Player() {
        setCurrentRoom("initial");
    }
}

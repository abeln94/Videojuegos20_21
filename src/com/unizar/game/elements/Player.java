package com.unizar.game.elements;

import com.unizar.game.Game;

/**
 * Represents the player
 */
public class Player extends NPC {

    public Player(String name) {
        super(name);
    }

    @Override
    public void register(Game game) {
        super.register(game);
    }

    @Override
    public void act() {
        // already done
    }

    @Override
    public String getDescription() {
        return describeContents("No llevas nada encima", "Llevas:");
    }

    @Override
    public void hear(String message) {
        game.addOutput(message);
    }
}

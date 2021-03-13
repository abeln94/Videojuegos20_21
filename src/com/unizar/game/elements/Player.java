package com.unizar.game.elements;

import com.unizar.game.Game;

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
    public void onHear(String message) {
        game.window.addOutput(message);
    }
}

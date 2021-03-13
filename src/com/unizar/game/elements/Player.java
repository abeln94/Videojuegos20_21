package com.unizar.game.elements;

public class Player extends NPC {
    public Player(String name) {
        super(name);
    }

    @Override
    public void onHear(String message) {
        game.window.addOutput(message);
    }
}

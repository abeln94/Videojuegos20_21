package com.unizar.game.elements;

/**
 * The player
 */
abstract public class Player extends NPC {
    public Player(String name, Class<? extends Element> holder) {
        super(name, holder);
    }
}

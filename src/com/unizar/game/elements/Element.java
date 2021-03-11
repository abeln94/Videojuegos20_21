package com.unizar.game.elements;

import com.unizar.game.Game;

import java.io.Serializable;

/**
 * A generic element of the game
 */
abstract public class Element implements Serializable {

    public final String name;

    public Element(String name) {
        this.name = name;
    }

    public String getDescription() {
        return name;
    }

    // ------------------------- game management -------------------------

    /**
     * The associated game
     */
    transient protected Game game;

    /**
     * Registers the active game on this element
     *
     * @param game the game to register
     */
    public void register(Game game) {
        this.game = game;
    }
}

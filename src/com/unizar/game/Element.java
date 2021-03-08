package com.unizar.game;

import java.io.Serializable;

/**
 * A generic element of the game
 */
abstract public class Element implements Serializable {

    // ------------------------- game actions -------------------------

    /**
     * This element turn. Do something (by default does nothing)
     */
    public void act() {
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
    public final void register(Game game) {
        this.game = game;
    }
}

package com.unizar.game.elements;

import com.unizar.game.Game;
import com.unizar.game.commands.Command;

import java.io.Serializable;
import java.util.function.Supplier;

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

    /**
     * Describe the current element
     */
    abstract public void describe();

    public Supplier<String> doCommand(Command command, NPC npc) {
        return null;
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

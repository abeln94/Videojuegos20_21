package com.unizar.game;

import java.io.Serializable;

/**
 * A generic room
 */
abstract public class Room implements Serializable {

    /**
     * The associated game
     */
    transient protected Game game;

    /**
     * Called when the player enters this room
     */
    abstract public void onEnter();

    /**
     * Called when the player enters a command while at this room
     *
     * @param command which command
     * @return the output message of the command, or null if the command is not valid
     */
    abstract public String onCommand(String command);

    // ------------------------- internal -------------------------

    /**
     * Registers the active game on this room
     *
     * @param game the game to register
     */
    public final void register(Game game) {
        this.game = game;
    }
}

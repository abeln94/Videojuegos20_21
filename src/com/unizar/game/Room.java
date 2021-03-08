package com.unizar.game;

/**
 * A generic room
 */
abstract public class Room extends Element {

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

}

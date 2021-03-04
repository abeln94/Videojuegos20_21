package com.unizar.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A game data.
 * Extend your game's data class
 * A list of rooms and a current one
 */
public abstract class Data implements Serializable {
    public static final String INITIAL = "__initial__";

    // ------------------------- abstract properties -------------------------

    /**
     * @return the title of the game (window's title)
     */
    abstract public String getTitle();

    // ------------------------- list of rooms -------------------------

    /**
     * List of available rooms
     */
    private final Map<String, Room> map = new HashMap<>();

    /**
     * Registers a room.
     * Use in the constructor of the specific game data
     *
     * @param name identifier of the room
     * @param room the room
     */
    protected void register(String name, Room room) {
        map.put(name, room);
    }

    /**
     * Registers a room as the initial one
     *
     * @param room the room
     */
    protected void registerInitial(Room room) {
        map.put(INITIAL, room);
    }

    /**
     * Returns the room associated with the given name
     *
     * @param name name of the room to retrieve
     * @return the room
     */
    public final Room getRoom(String name) {
        Room room = map.get(name);
        if (room == null) {
            throw new RuntimeException("Unknown room: " + name);
        }
        return room;
    }

    // ------------------------- game registration -------------------------

    /**
     * Registers the current game on all the rooms
     *
     * @param game current game
     */
    public final void register(Game game) {
        map.forEach((key, room) -> room.register(game));
    }

    // ------------------------- current room -------------------------

    private String current = null;

    /**
     * @return the current kept room
     */
    public String getCurrentRoom() {
        return current == null ? INITIAL : current;
    }

    /**
     * @param current the room to keep
     */
    public void setCurrentRoom(String current) {
        this.current = current;
    }

}

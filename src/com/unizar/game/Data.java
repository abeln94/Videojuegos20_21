package com.unizar.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A game data.
 * Extend your game's data class
 * A list of rooms (includes the current one) + a list of players
 */
public abstract class Data implements Serializable {
    public static final String START_SCREEN = "__start_screen__";

    // ------------------------- abstract properties -------------------------

    /**
     * @return the title of the game (window's title)
     */
    abstract public String getTitle();

    /**
     * Return the path of an image
     *
     * @param label the label of the image
     * @return the path of the image
     */
    abstract public String getImagePath(String label);

    // ------------------------- list of rooms -------------------------

    /**
     * List of available rooms
     */
    private final Map<String, Room> rooms = new HashMap<>();

    /**
     * Registers a room.
     * Use in the constructor of the specific game data
     *
     * @param name identifier of the room. Use {@link #START_SCREEN} for the starting screen
     * @param room the room
     */
    protected final void register(String name, Room room) {
        rooms.put(name, room);
    }

    /**
     * Returns the room associated with the given name
     *
     * @param name name of the room to retrieve
     * @return the room
     * @throws RuntimeException if there is no room with that name
     */
    public final Room getRoom(String name) {
        Room room = rooms.get(name);
        if (room == null) {
            throw new RuntimeException("Unknown room: " + name);
        }
        return room;
    }

    // ------------------------- list of npcs -------------------------

    /**
     * List of available npcs
     */
    private final Map<String, NPC> npcs = new HashMap<>();

    /**
     * Register a new NPC
     *
     * @param name name of the npc
     * @param npc  the npc
     * @throws RuntimeException if there is no npc with that name
     */
    protected final void register(String name, NPC npc) {
        npcs.put(name, npc);
    }

    /**
     * Returns a NPC by name
     *
     * @param name name of the npc
     * @return that npc
     */
    public final NPC getNPC(String name) {
        NPC npc = npcs.get(name);
        if (npc == null) {
            throw new RuntimeException("Unknown npc: " + name);
        }
        return npc;
    }


    // ------------------------- game registration -------------------------

    /**
     * Registers the current game on all the rooms
     *
     * @param game current game
     */
    public final void register(Game game) {
        rooms.values().forEach(e -> e.register(game));
        npcs.values().forEach(e -> e.register(game));
    }

    /**
     * Make the rooms and npcs act
     */
    public void act() {
        rooms.values().forEach(Element::act);
        npcs.values().forEach(Element::act);
    }

    // ------------------------- current room -------------------------

    private String current = null;

    /**
     * @return the current kept room (or the start screen if setCurrentRoom wasn't called)
     */
    public String getCurrentRoom() {
        return current == null ? START_SCREEN : current;
    }

    /**
     * @param current the room to keep
     */
    public void setCurrentRoom(String current) {
        this.current = current;
    }

}

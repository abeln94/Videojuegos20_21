package com.unizar.game;

import com.unizar.hobbit.Game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Data implements Serializable {
    public static final String INITIAL = "__initial__";

    // properties

    abstract public String getTitle();

    // rooms
    private final Map<String, Room> map = new HashMap<>();

    protected void register(String name, Room room) {
        map.put(name, room);
    }

    protected void registerInitial(Room room){
        map.put(INITIAL,room);
    }

    public final Room getRoom(String name) {
        Room room = map.get(name);
        if (room == null) {
            throw new RuntimeException("Unknown room: " + name);
        }
        return room;
    }

    // internal

    public final void register(Game game) {
        map.forEach((key, room) -> room.register(game));
    }

    // current room

    private String current = null;

    public String getCurrentRoom() {
        return current == null ? INITIAL : current;
    }

    public void setCurrentRoom(String current) {
        this.current = current;
    }

}

package com.unizar.game.elements;

import com.unizar.game.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A generic element of the game
 */
abstract public class Element implements Serializable {

    public final String name;

    public boolean global;

    public final List<Element> elements = new ArrayList<>();

    public Element(String name) {
        this.name = name;
    }

    public String getDescription(NPC npc) {
        return name;
    }

    public List<Element> getInteractable() {
        return elements;
    }

    public void act() {
        // do nothing
    }


    public void say(NPC npc, String message) {
        elements.stream()
                .filter(e -> e instanceof NPC)
                .filter(e -> e != npc)
                .forEach(e -> ((NPC) e).onHear(message));
    }

    @Override
    public String toString() {
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

    public void init() {
    }
}

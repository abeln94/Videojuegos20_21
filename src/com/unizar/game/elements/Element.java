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

    public final List<Class<? extends Element>> elements = new ArrayList<>();

    public Element(String name) {
        this.name = name;
    }

    public String getDescription(Class<? extends NPC> npc) {
        return name;
    }

    public List<Class<? extends Element>> getInteractable() {
        return elements;
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

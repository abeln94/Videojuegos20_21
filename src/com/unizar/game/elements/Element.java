package com.unizar.game.elements;

import com.unizar.game.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A generic element of the game
 */
abstract public class Element implements Serializable {

    /**
     * This element's name
     */
    public final String name;

    /**
     * Whether this element can be interacted from anywhere
     */
    public boolean global;

    /**
     * List of elements inside this element
     */
    public final List<Element> elements = new ArrayList<>();

    public Element(String name) {
        this.name = name;
    }

    /**
     * Returns the description of this element from the perspective of a given npc
     *
     * @param npc npc looking for a description of this element
     * @return the description as string
     */
    public String getDescription(NPC npc) {
        return name;
    }

    /**
     * @return all the interactable elements from this one
     */
    public List<Element> getInteractable() {
        List<Element> interactable = new ArrayList<>(elements);
        elements.forEach(i -> interactable.addAll(i.getInteractable()));
        return interactable;
    }

    /**
     * This element's turn
     */
    public void act() {
        // do nothing
    }

    /**
     * Makes an npc say something to this element
     *
     * @param npc     who said the message
     * @param message what was said
     */
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

    /**
     * Initializes this element.
     * Make sure to call super.init() AFTER your data
     */
    public void init() {
    }
}

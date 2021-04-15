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
     * The weight of this element (if npc, can carry only other elements with smaller weight).
     */
    public int weight = Integer.MAX_VALUE;

    /**
     * List of elements inside this element
     */
    public final List<Element> elements = new ArrayList<>();

    /**
     * Whether this element is alive or not
     */
    public boolean alive = true;

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
     * Makes an npc say something to all the other npcs on this location
     *
     * @param npc     who said the message
     * @param message what was said
     */
    public void notifyNPCs(NPC npc, String message) {
        elements.stream()
                .filter(e -> e instanceof NPC)
                .filter(e -> e != npc)
                .forEach(e -> e.hear(message));
    }

    /**
     * When this element hears something
     *
     * @param message what was heard
     */
    public void hear(String message) {
    }

    @Override
    public String toString() {
        return name;
    }

    // ------------------------- game management -------------------------

    /**
     * The associated game
     */
    public transient Game game;

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
        elements.forEach(element -> {
            if (element instanceof NPC) {
                ((NPC) element).location = this;
            }
        });
    }
}

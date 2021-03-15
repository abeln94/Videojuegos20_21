package com.unizar.game;

import com.unizar.game.elements.Element;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A game world.
 * Extend your game's world class, then in the constructor set the properties and add the elements
 */
public abstract class World implements Serializable {

    // ------------------------- properties -------------------------

    /**
     * Properties of this world.
     */
    public Properties properties;


    // ------------------------- elements -------------------------

    /**
     * List of available elements.
     */
    public Set<Element> elements = new HashSet<>();

    /**
     * @return the global elements (those marked with the global flag)
     */
    public List<Element> getGlobalElements() {
        return elements.stream().filter(e -> e.global).collect(Collectors.toList());
    }


    // ------------------------- game registration -------------------------

    /**
     * Registers the current game on all the elements
     *
     * @param game current game
     */
    public void register(Game game) {
        elements.forEach(e -> e.register(game));
    }

    /**
     * Initializes this world
     */
    public void init() {
        elements.forEach(Element::init);
    }

}

package com.unizar.game;

import com.unizar.game.elements.Element;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A game data.
 * Extend your game's data class
 * A list of rooms (includes the current one) + a list of npcs
 */
public abstract class Data implements Serializable {

    // ------------------------- properties -------------------------

    public Properties properties;

    protected void register(Properties properties) {
        this.properties = properties;
    }


    // ------------------------- elements -------------------------

    /**
     * List of available elements
     */
    public Set<Element> elements = new HashSet<>();

    /**
     * Registers an element.
     * Use in the constructor of the specific game data
     *
     * @param element the element
     */
    protected void register(Element element) {
        elements.add(element);
    }

//    public final List<Element> getElementsMatching(Predicate<? super Element> match) {
//        return elements.stream().filter(match).collect(Collectors.toList());
//    }


    // ------------------------- game registration -------------------------

    /**
     * Registers the current game on all the elements
     *
     * @param game current game
     */
    public void register(Game game) {
        elements.forEach(e -> e.register(game));
    }

    public void init() {
        elements.forEach(Element::init);
    }
}

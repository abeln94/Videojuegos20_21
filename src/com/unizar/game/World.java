package com.unizar.game;

import com.unizar.Utils;
import com.unizar.game.elements.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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

    // ------------------------- objectives -------------------------

    /**
     * Objectives
     */
    public List<Utils.Pair<String, Function<Game, Boolean>>> requiredObjectives = new ArrayList<>();
    public List<Function<Game, Boolean>> optionalObjectives = new ArrayList<>();
    public int totalObjectives = 0;

    public void requiredObjective(String help, Function<Game, Boolean> isCompleted) {
        requiredObjectives.add(Utils.Pair.of(help, isCompleted));
        totalObjectives++;
    }

    public void optionalObjective(Function<Game, Boolean> isCompleted) {
        optionalObjectives.add(isCompleted);
        totalObjectives++;
    }


    // ------------------------- elements -------------------------

    /**
     * List of available elements.
     */
    public Set<Element> elements = new HashSet<>();


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

package com.unizar.game;

import com.unizar.Utils;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.NPC;

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
    // ---------------------------- time ----------------------------
    private int time = 0;

    public boolean night = true;

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

    public void act() {
        time = (time + 1) % 20; // cada vez que wait avanza una hora, 0-9 noche, 10-19 dia
        System.out.println("Current time: " + time);

        if (time == 10) {
            night = false;
            elements.stream().filter(element -> element instanceof NPC).forEach(element -> element.hear("Se hace de día"));
        }
        if (time == 0) {
            night = true;
            elements.stream().filter(element -> element instanceof NPC).forEach(element -> element.hear("Se hace de noche"));
        }
    }

}

package com.unizar.game;

import com.unizar.game.commands.Word;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.NPC;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

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

    abstract public boolean playerWon(Game game);

    // ------------------------- elements -------------------------

    /**
     * List of available elements.
     */
    public Set<Element> elements = new LinkedHashSet<>(); // must retain inserted order


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

    // ------------------------- world action -------------------------

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

    // ------------------------- constructor -------------------------

    protected class WorldBuilder {
        Element current;

        private WorldBuilder(Element current) {
            this.current = current;
        }

        public WorldBuilder with(Element element) {
            // register
            World.this.elements.add(element);
            // and add as child
            current.elements.add(element);
            return this;
        }

        public WorldBuilder with(WorldBuilder builder) {
            // don't register
            // but add as child
            current.elements.add(builder.current);
            return this;
        }

        public WorldBuilder withHidden(Word.Action action, Element element) {
            // register
            World.this.elements.add(element);
            // add to hidden
            current.hiddenElements.put(action, element);
            return this;
        }

        public WorldBuilder withHidden(Word.Action action, WorldBuilder builder) {
            // don't register
            // add to hidden
            current.hiddenElements.put(action, builder.current);
            return this;
        }
    }

    public WorldBuilder add(Element element) {
        elements.add(element);
        return new WorldBuilder(element);
    }

}

package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.Game;
import com.unizar.game.Objective;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.EngineException;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A generic element of the game
 */
abstract public class Element implements Serializable {

    // ------------------------- properties -------------------------

    /**
     * This element's name
     */
    public String name;

    /**
     * The weight of this element (if npc, can carry only other elements with smaller weight).
     */
    public int weight = Integer.MAX_VALUE;

    /**
     * List of elements inside this element
     */
    public final Set<Element> elements = new HashSet<>();

    /**
     * Elements that will be made visible (moved to elements) when the specific action is performed.
     * Only some actions will trigger this.
     */
    public final Map<Word.Action, Element> hiddenElements = new HashMap<>();

    // ------------------------- objectives -------------------------

    public int totalObjectives = 0;

    public Set<Objective> pendingObjectives = new HashSet<>();

    public void addObjective(Objective objective) {
        pendingObjectives.add(objective);
        totalObjectives++;
    }

    // ------------------------- functions -------------------------

    public Element(String name) {
        this.name = name;
    }

    /**
     * Returns the description of this element (from the perspective of the player)
     *
     * @return the description as string
     */
    public String getDescription() {
        return name + describeContents(".", ": Contiene");
    }

    /**
     * Returns the description of the contents of this element (excludes the player)
     *
     * @param ifEmpty if there are no elements, returns this string instead
     * @param prefix  if there are elements, appends this prefix and a line break
     * @return either ifEmpty or prefix+"\n"+contents
     */
    public String describeContents(String ifEmpty, String prefix) {
        final String contents = elements.stream()
                .filter(e -> e != game.getPlayer())
                .map(Element::getDescription)
                .map(v -> "-" + v)
                .map(Utils::increasePadding)
                .collect(Collectors.joining("\n"));
        return contents.isEmpty() ? ifEmpty : prefix + "\n" + contents;
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
     * Makes an npc say something to all the other npcs on this element
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

    /**
     * @return Returns the parent of this element (null if nowhere)
     */
    public Element getLocation() {
        return game.world.elements.stream().filter(e -> e.elements.contains(this)).findFirst().orElse(null);
    }

    /**
     * Moves this element to another parent
     *
     * @param newParent the new parent
     */
    public void moveTo(Element newParent) {
        assert newParent != this;
        final Element parent = getLocation();
        if (parent != null) parent.elements.remove(this);
        if (newParent != null) newParent.elements.add(this);
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
     */
    public void init() {
    }

    public boolean canExecute(Command command) {
        return false;
    }

    public Result execute(Command command) throws EngineException {
        throw new EngineException("No puedo hacer eso");
    }
}

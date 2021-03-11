package com.unizar.game;

import com.unizar.game.elements.Element;
import com.unizar.game.elements.Holdable;
import com.unizar.game.elements.NPC;
import com.unizar.game.elements.Player;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public final Set<Element> elements = new HashSet<>();

    /**
     * Registers an element.
     * Use in the constructor of the specific game data
     *
     * @param element the element
     */
    protected final void register(Element element) {
        elements.add(element);
    }

//    public final List<Element> getElementsMatching(Predicate<? super Element> match) {
//        return elements.stream().filter(match).collect(Collectors.toList());
//    }

    public final List<Element> getInteractables(NPC npc) {
        Class<? extends Element> holder = npc.getHolder();
        if (holder == null) return Collections.emptyList();

        return getElements(Element.class).stream()
                .filter(e -> {
                    if (e.getClass() == holder) return true;
                    if (!(e instanceof Holdable)) return false;
                    return ((Holdable) e).getHolder() == holder || ((Holdable) e).getHolder() == npc.getClass();
                })
                .collect(Collectors.toList());
    }

    public final <T> List<T> getElements(Class<T> name) {
        return (List<T>) elements.stream()
                .filter(name::isInstance)
                .collect(Collectors.toList());
    }

    /**
     * Returns the first element associated with the given class
     */
    public final <T> T getElement(Class<T> name) {
        return getElements(name).stream().findFirst().orElseThrow();
    }

    public Player getPlayer() {
        return getElement(Player.class);
    }

    public List<Holdable> getPlayerVisible() {
        return elements.stream()
                .filter(e -> e instanceof Holdable)
                .map(e -> (Holdable) e)
                .filter(e -> e.getHolder() == getPlayer().getHolder() && e != getPlayer())
                .collect(Collectors.toList());
    }

    /**
     * Make the rooms and npcs act
     */
    public void nonPlayerAct() {
        elements.stream().filter(e -> !(e instanceof Player)).forEach(Element::act);
    }

    // ------------------------- game registration -------------------------

    /**
     * Registers the current game on all the elements
     *
     * @param game current game
     */
    public final void register(Game game) {
        elements.forEach(e -> e.register(game));
    }
}

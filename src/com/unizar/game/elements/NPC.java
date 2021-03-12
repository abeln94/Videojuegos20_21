package com.unizar.game.elements;

import com.unizar.game.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A generic NPC
 */
abstract public class NPC extends Element {

    public Class<? extends Element> location; // an element because you can be inside an item

    public Set<Class<? extends Element>> elements = new HashSet<>();

    public NPC(String name) {
        super(name);
    }

    public void changeLocation(Class<? extends Element> newLocation) {
        game.getElement(location).elements.remove(this.getClass());
        location = newLocation;
        game.getElement(location).elements.add(this.getClass());
    }

    @Override
    public String getDescription(Class<? extends NPC> npc) {
        String prefix = ": " + super.name + " lleva ";

        return super.getDescription(npc) + Utils.generateList("", prefix, prefix, elements.stream().map(e -> game.getElement(e).getDescription(npc)).collect(Collectors.toList()));
    }
}

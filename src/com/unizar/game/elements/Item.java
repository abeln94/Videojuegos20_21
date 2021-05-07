package com.unizar.game.elements;

import com.unizar.Utils;

import java.util.stream.Collectors;

/**
 * An item.
 * Can be closeable
 */
public abstract class Item extends Element {

    public enum OPENABLE {
        OPENED, // the element is open
        CLOSED, // the element is closed (and unlocked)
        LOCKED // the element is locked (and closed)
    }

    /**
     * If null, this item is not openable
     * If not null, this item is openable (and its value corresponds to its state)
     */
    public OPENABLE openable = null;

    /**
     * If the element is locked, you need this element to unlock it (if null, this can't be unlocked)
     */
    public Element lockedWith = null;

    //la fuerza del objeto
    public int fuerza = 0; //modificador del daño que hace, la flecha por ejemplo será un 1000, el resto debería ser de 1 a 5

    //idioma en que está escrito
    public String idioma = null;

    public Item(String name) {
        super(name);
    }

    @Override
    public String getDescription(NPC npc) {
        String prefix = ": Contiene";

        return this + Utils.joinList("", prefix, prefix, elements.stream().map(e -> e.getDescription(npc)).collect(Collectors.toList()));
    }

    /**
     * An npc requested to examine this item
     *
     * @param npc which npc is examining the item
     * @return the examination result
     */
    public String examine(NPC npc) {
        return "Puedes ver " + getDescription(npc) + ".";
    }

}

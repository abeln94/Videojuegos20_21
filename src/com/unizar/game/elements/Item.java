package com.unizar.game.elements;

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

    public Item(String name) {
        super(name);
    }

    /**
     * An npc requested to examine this item
     *
     * @param npc which npc is examining the item
     * @return the examination result
     */
    public String examine(NPC npc) {
        return "Es " + getDescription();
    }

}

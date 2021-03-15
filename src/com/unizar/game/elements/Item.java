package com.unizar.game.elements;

/**
 * An item.
 * Can be closeable
 */
public abstract class Item extends Element {

    /**
     * If null, this item is not openable
     * If not null, this item is openable (and this value is whether the item is opened or not)
     */
    public Boolean opened = null;

    public Item(String name) {
        super(name);
    }

}

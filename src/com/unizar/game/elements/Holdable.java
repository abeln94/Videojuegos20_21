package com.unizar.game.elements;

/**
 * Something that can be holded
 */
abstract public class Holdable extends Element {


    public Holdable(String name, Class<? extends Element> holder) {
        this.name = name;
        this.holder = holder;
    }

    // ------------------------- identifier -------------------------

    public final String name;


    // ------------------------- parent -------------------------

    private Class<? extends Element> holder;

    /**
     * @return the thing that contains this element
     */
    public Class<? extends Element> getHolder() {
        return holder;
    }

    /**
     * @param holder the thing that contains this element
     */
    public void setHolder(Class<? extends Element> holder) {
        this.holder = holder;
    }
}

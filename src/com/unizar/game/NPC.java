package com.unizar.game;

/**
 * A generic NPC
 */
abstract public class NPC extends Element {

    // ------------------------- current room -------------------------

    private String current = null;

    /**
     * @return the current room of this element
     */
    public String getCurrentRoom() {
        return current;
    }

    /**
     * @param current the room to set
     */
    public void setCurrentRoom(String current) {
        this.current = current;
    }


}

package com.unizar.game.commands;

public enum Direction {
    NORTH("norte"),
    NORTHEAST("noreste"),
    SOUTH("sur"),
    NORTHWEST("noroeste"),
    EAST("este"),
    SOUTHEAST("sureste"),
    WEST("oeste"),
    SOUTHWEST("sureste"),
    UP("arriba"),
    DOWN("abajo"),
    ;

    Direction(String name) {
        this.name = name;
    }

    // ------------------------- identifier -------------------------

    public final String name;
}

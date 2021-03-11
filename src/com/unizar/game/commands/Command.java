package com.unizar.game.commands;

public enum Command {
    Wait("esperar"),

    GoNorth("norte"),
    GoSouth("sur"),
    GoEast("este"),
    GoWest("oeste"),

    READ("leer"),
    OPEN("abrir");

    Command(String name) {
        this.name = name;
    }

    // ------------------------- identifier -------------------------

    public final String name;
}

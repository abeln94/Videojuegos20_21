package com.unizar.game.elements;

import com.unizar.game.commands.Command;

import java.util.function.Supplier;

/**
 * A generic room
 */
abstract public class Room extends Element {

    public final String image;

    protected Room(String image) {
        this.image = image;
    }

    @Override
    public Supplier<String> doCommand(Command command, NPC npc) {
        switch (command) {
            case GoNorth -> {
                return () -> "No hay nada al norte";
            }
            case GoSouth -> {
                return () -> "No hay nada al sur";
            }
            case GoEast -> {
                return () -> "No hay nada al este";
            }
            case GoWest -> {
                return () -> "No hay nada al oeste";
            }
        }
        return super.doCommand(command, npc);
    }
}

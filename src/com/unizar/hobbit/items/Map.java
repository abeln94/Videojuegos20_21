package com.unizar.hobbit.items;

import com.unizar.game.commands.Command;
import com.unizar.game.elements.Holdable;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.npcs.Gandalf;

import java.util.function.Supplier;

public class Map extends Holdable {

    public Map() {
        super("mapa|peculiar", Gandalf.class);
    }

    @Override
    public void describe() {
    }

    @Override
    public Supplier<String> doCommand(Command command, NPC npc) {
        switch (command) {
            case READ -> {
                if (npc.getClass() == getHolder())
                    // only the holder can read
                    return () -> "Lees el mapa peculiar. Parece que contiene símbolos, pero no sabes leerlos.";
            }
        }
        return super.doCommand(command, npc);
    }
}

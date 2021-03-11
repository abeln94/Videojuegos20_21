package com.unizar.hobbit.rooms;

import com.unizar.game.commands.Command;
import com.unizar.game.elements.NPC;
import com.unizar.game.elements.Room;

import java.util.function.Supplier;

public class EastRoom extends Room {

    public EastRoom() {
        super("3");
    }

    @Override
    public void describe() {
        game.addDescription("Estás en un terreno vacío y sombrío con colinas lúgubres a lo lejos.");
        game.addDescription("Al oeste está la puerta verde redonda. Las salidas visibles son: este, norte y noreste.");
    }

    @Override
    public Supplier<String> doCommand(Command command, NPC npc) {
        switch (command) {
            case GoWest -> {
                return () -> {
                    if (game.data.getElement(StartRoom.class).doorOpen) {
                        npc.setHolder(StartRoom.class);
                        return "Regresas a la habitación";
                    } else {
                        return "La puerta está cerrada";
                    }
                };
            }
        }
        return super.doCommand(command, npc);
    }
}

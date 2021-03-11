package com.unizar.hobbit.rooms;

import com.unizar.game.commands.Command;
import com.unizar.game.elements.NPC;
import com.unizar.game.elements.Room;

import java.util.function.Supplier;

public class StartRoom extends Room {

    public boolean doorOpen = false;

    public StartRoom() {
        super("2");
    }

    @Override
    public void describe() {
        game.addDescription("Te encuentras en una sala alargada confortable.");
        game.addDescription("Al este está la puerta verde redonda, " + (doorOpen ? "abierta" : "cerrada") + ".");
    }

    @Override
    public Supplier<String> doCommand(Command command, NPC npc) {
        switch (command) {
            case GoEast -> {
                return () -> {
                    if (doorOpen) {
                        npc.setHolder(EastRoom.class);
                        return "Sales de la habitación";
                    } else {
                        return "La puerta está cerrada";
                    }
                };
            }
        }
        return super.doCommand(command, npc);
    }
}

package com.unizar.hobbit.npcs;

import com.unizar.game.commands.Command;
import com.unizar.game.elements.NPC;
import com.unizar.game.elements.Player;
import com.unizar.hobbit.rooms.StartRoom;

import java.util.function.Supplier;

public class Bilbo_Player extends Player {
    public Bilbo_Player() {
        super("Bilbo", StartRoom.class);
    }

    @Override
    public Supplier<String> doCommand(Command command, NPC npc) {
        if (command == Command.Wait && npc == this) return () -> "Esperas. El tiempo pasa...";

        return super.doCommand(command, npc);
    }

    @Override
    public void describe() {
        // nothing
    }
}

package com.unizar.hobbit.items;

import com.unizar.game.commands.Command;
import com.unizar.game.elements.Holdable;
import com.unizar.game.elements.NPC;

import java.util.function.Supplier;

public class GreenDoor {

    public static final String NAME = "la puerta verde pequeña";

    public static class StartRoom extends Holdable {

        public boolean isOpen = false;

        public StartRoom() {
            super(NAME, StartRoom.class);
        }

        @Override
        public void describe() {
        }

        @Override
        public Supplier<String> doCommand(Command command, NPC npc) {
            switch (command) {
                case OPEN -> {
                    if (isOpen) {
                        return () -> "La puerta ya está abierta";
                    } else {
                        isOpen = true;
                        return () -> "Abres la puerta";
                    }
                }
            }
            return super.doCommand(command, npc);
        }
    }

    public static class EastRoom extends Holdable {

        public EastRoom() {
            super(NAME, EastRoom.class);
        }

        @Override
        public void describe() {
            game.addDescription("Al oeste está la puerta verde pequeña");
        }
    }
}

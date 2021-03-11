package com.unizar.hobbit.items;

import com.unizar.game.elements.Holdable;
import com.unizar.hobbit.rooms.StartRoom;

public class StartChest extends Holdable {
    public StartChest() {
        super("cofre|madera", StartRoom.class);
    }

    @Override
    public void describe() {
        game.addDescription("El cofre de madera.");
    }
}

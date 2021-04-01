package com.unizar.game.elements;

import com.unizar.game.Game;

import java.util.stream.Collectors;

/**
 * Represents the player
 */
public class Player extends NPC {

    public Player(String name) {
        super(name);
    }

    @Override
    public void register(Game game) {
        super.register(game);
    }

    @Override
    public void act() {
        // already done
    }

    @Override
    public String getDescription(NPC npc) {
        // from the perspective of another npc? return as a generic npc
        if (npc != this) return super.getDescription(npc);

        // return inventory
        return elements.isEmpty()
                ? "No llevas nada encima"
                : "Llevas:\n - " + elements.stream().map(e -> e.getDescription(this)).collect(Collectors.joining(".\n - "));
    }

    @Override
    public void hear(String message) {
        game.window.addOutput(message);
    }
}

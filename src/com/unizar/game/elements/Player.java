package com.unizar.game.elements;

import com.unizar.Utils;
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
        // TODO: consider adding a 'demo' that plays automatically
    }

    @Override
    public void ask(NPC npc, String message) {
        hear(npc + " dice: \"" + message + "\"");
    }

    @Override
    public String getDescription() {
        String elements = describeContents("No llevas nada encima.", "Llevas:");
        String contents = wearables.stream()
                .map(Element::getDescription)
                .map(v -> "-" + v)
                .map(Utils::increasePadding)
                .collect(Collectors.joining("\n"));
        contents = wearables.isEmpty() ? "No llevas nada puesto." : "Llevas puesto:\n" + contents;

        return elements + "\n" + contents;
    }

    @Override
    public void hear(String message) {
        game.addOutput(message);
    }
}

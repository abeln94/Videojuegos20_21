package com.unizar.game.elements;

import com.unizar.game.Game;
import com.unizar.game.Utils;
import com.unizar.game.commands.Direction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A generic location
 */
abstract public class Location extends Element {

    public final String image;

    public Map<Direction, Class<? extends Element>> exits = new HashMap<>();

    public final Set<Class<? extends Element>> elements = new HashSet<>();

    public Location(String name, String image) {
        super(name);
        this.image = image;
    }

    @Override
    public String getDescription() {
        StringBuilder description = new StringBuilder(super.getDescription());

        List<String> visibleExits = exits.entrySet().stream().filter(e -> {
            if (!(game.getElement(e.getValue()) instanceof Location)) {
                description.append(". Al " + e.getKey().name + " está " + game.getElement(e.getValue()).name);
                return false;
            }
            return true;
        }).map(e -> e.getKey().name).collect(Collectors.toList());
        description.append(Utils.generateList("", ". Hay una salida al ", ". Hay salidas visibles al ", visibleExits));

        description.append(" Puedes ver:");
        if (elements.isEmpty()) {
            description.append(" - Nada");
        } else {
            boolean dot = false;
            for (Class<? extends Element> e : elements) {
                String d = game.getElement(e).getDescription();
                if (d != null) {
                    description.append((dot ? "." : "") + "\n - " + d);
                    dot = true;
                }
            }
        }

        return description.toString();
    }

    @Override
    public void register(Game game) {
        super.register(game);
        // register all the NPC on this room so that their location is this room
        elements.stream()
                .map(game::getElement)
                .filter(e -> e instanceof NPC)
                .forEach(npc -> ((NPC) npc).location = this.getClass());
    }
}

package com.unizar.game.elements;

import com.unizar.game.Game;
import com.unizar.game.Utils;
import com.unizar.game.commands.Word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A generic location
 */
abstract public class Location extends Element {

    public final String image;

    public Map<Word.Direction, Utils.Pair<Class<? extends Location>, Class<? extends Item>>> exits = new HashMap<>();

    public Location(String name, String image) {
        super(name);
        this.image = image;
    }

    @Override
    public List<Class<? extends Element>> getInteractable() {
        List<Class<? extends Element>> exitItems = exits.values().stream().map(le -> le.second).filter(Objects::nonNull).collect(Collectors.toList());

        exitItems.addAll(super.getInteractable());

        return exitItems;
    }

    @Override
    public String getDescription(Class<? extends NPC> npc) {
        StringBuilder description = new StringBuilder(super.getDescription(npc) + ".");

        List<String> visibleExits = exits.entrySet().stream().filter(e -> {
            if (e.getValue().second != null) {
                description.append(". Al " + e.getKey().name + " está " + game.getElement(e.getValue().second).name);
                return false;
            }
            return true;
        }).map(e -> e.getKey().name).collect(Collectors.toList());
        description.append(Utils.generateList("", ". Hay una salida al ", ". Hay salidas visibles al ", visibleExits));

        description.append(" Puedes ver:");
        List<Class<? extends Element>> visible = elements.stream().filter(e -> e != npc).collect(Collectors.toList());
        if (visible.isEmpty()) {
            description.append("\n - Nada");
        } else {
            boolean dot = false;
            for (Class<? extends Element> e : visible) {
                String d = game.getElement(e).getDescription(npc);
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

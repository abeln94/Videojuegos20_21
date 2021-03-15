package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.commands.Word;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A generic location
 */
abstract public class Location extends Element {

    /**
     * The image of this location (from the game's data image folder)
     */
    public final String image;

    /**
     * List of exits from this location. A map of
     * key -> the direction
     * value -> the new location + null, if you can travel freely, or the new location + an item that you need to traverse
     */
    public Map<Word.Direction, Utils.Pair<Location, Item>> exits = new HashMap<>();

    public Location(String name, String image) {
        super(name);
        this.image = image;
    }

    @Override
    public List<Element> getInteractable() {
        List<Element> interactable = new ArrayList<>();

        interactable.addAll(super.getInteractable());

        interactable.addAll(exits.values().stream().map(le -> le.second).filter(Objects::nonNull).collect(Collectors.toList()));

        return interactable;
    }

    @Override
    public String getDescription(NPC npc) {
        StringBuilder description = new StringBuilder(super.getDescription(npc));

        List<String> visibleExits = exits.entrySet().stream().filter(e -> {
            if (e.getValue().second != null) {
                description.append(". Al " + e.getKey().name + " está " + e.getValue().second);
                return false;
            }
            return true;
        }).map(e -> e.getKey().name).collect(Collectors.toList());
        description.append(Utils.joinList("", ". Hay una salida al", ". Hay salidas visibles al", visibleExits));

        description.append(" Puedes ver:");
        List<Element> visible = elements.stream().filter(e -> e != npc).collect(Collectors.toList());
        if (visible.isEmpty()) {
            description.append("\n - Nada");
        } else {
            boolean dot = false;
            for (Element e : visible) {
                String d = e.getDescription(npc);
                if (d != null) {
                    description.append((dot ? "." : "") + "\n - " + d);
                    dot = true;
                }
            }
        }

        return description.toString();
    }
}

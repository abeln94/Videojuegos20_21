package com.unizar.game.elements;

import com.unizar.Utils;
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

    public Map<Word.Direction, Utils.Pair<Location, Item>> exits = new HashMap<>();

    public Location(String name, String image) {
        super(name);
        this.image = image;
    }

    @Override
    public List<Element> getInteractable() {
        List<Element> exitItems = exits.values().stream().map(le -> le.second).filter(Objects::nonNull).collect(Collectors.toList());

        exitItems.addAll(super.getInteractable());

        return exitItems;
    }

    public List<Element> getOtherNPC(NPC npc) {
        return elements.stream().filter(e -> e instanceof NPC).filter(e -> e != npc).collect(Collectors.toList());
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
        description.append(Utils.generateList("", ". Hay una salida al ", ". Hay salidas visibles al ", visibleExits));

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


    @Override
    public void init() {
        // register all the NPC on this room so that their location is this room
        elements.stream()
                .filter(e -> e instanceof NPC)
                .forEach(npc -> ((NPC) npc).location = this);
        super.init();
    }
}

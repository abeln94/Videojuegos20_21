package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.Objective;
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
    public String image;

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
    public String getDescription() {
        // start with the location description
        StringBuilder description = new StringBuilder(name + ".");

        // add the object exits
        List<String> visibleExits = exits.entrySet().stream().filter(e -> {
            if (e.getValue().second != null) {
                description.append(" Hacia " + e.getKey().description + " está " + e.getValue().second + ".");
                return false;
            }
            return true;
        }).map(e -> e.getKey().description).collect(Collectors.toList());
        description.append(Utils.joinList("", " Hay una salida hacia", " Hay salidas visibles hacia", visibleExits));

        // add the elements descriptions
        description.append(describeContents(" No ves nada mas.", " Puedes ver:"));

        return description.toString();
    }

    @Override
    public void init() {
        // add a new objective: visit this location
        addObjective(new Objective() {
            @Override
            public boolean isCompleted() {
                return game.getPlayer().getLocation() == Location.this;
            }
        });
        super.init();
    }
}

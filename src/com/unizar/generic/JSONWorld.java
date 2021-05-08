package com.unizar.generic;

import com.unizar.Utils;
import com.unizar.game.Game;
import com.unizar.game.World;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A World generated from JSON files
 */
public class JSONWorld extends World {

    // ------------------------- files -------------------------
    public static final String FILE_NPCS = "/npcs.json";
    public static final String FILE_ITEMS = "/items.json";
    public static final String FILE_LOCATIONS = "/locations.json";
    static final String FILE_PROPERTIES = "/properties.json";

    // ------------------------- completion -------------------------

    private Element winItem;
    private Element winLocation;

    @Override
    public boolean playerWon(Game game) {
        return winLocation.elements.contains(winItem);
    }

    // ------------------------- loading -------------------------


    public JSONWorld(String root) throws IOException {
        // properties
        final JSONObject json_properties = new JSONObject(Utils.readFile(root + FILE_PROPERTIES));
        this.properties = new JSONProperties(root, json_properties);

        // elements
        elements = loadElements(root);

        // completion
        winItem = elements.stream().filter(e -> e.id.equals(json_properties.getString("winItem"))).findFirst().orElse(null);
        winLocation = elements.stream().filter(e -> e.id.equals(json_properties.getString("winLocation"))).findFirst().orElse(null);
    }

    public static Set<Element> loadElements(String path) throws IOException {

        final JSONArray npcs = new JSONArray(Utils.readFile(path + FILE_NPCS));
        final JSONArray items = new JSONArray(Utils.readFile(path + FILE_ITEMS));
        final JSONArray locations = new JSONArray(Utils.readFile(path + FILE_LOCATIONS));

        Map<String, Element> elements = new HashMap<>();

        // initialize empty elements
        for (int i = 0; i < npcs.length(); i++) {
            JSONObject npc = npcs.getJSONObject(i);
            final String id = npc.getString("id");
            if (id.contains("Player"))
                elements.put(id, new Player(npc.getString("name")) {
                });
            else elements.put(id, new NPC(npc.getString("name")) {
            });

        }
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            elements.put(item.getString("id"), new Item(item.getString("name")) {
            });
        }
        for (int i = 0; i < locations.length(); i++) {
            JSONObject location = locations.getJSONObject(i);
            elements.put(location.getString("id"), new Location(location.getString("name"), location.getString("image"), location.getString("music")) {
            });
        }

        // set properties

        // Element
        final JSONArray all = new JSONArray();
        all.putAll(npcs);
        all.putAll(items);
        all.putAll(locations);
        for (int i = 0; i < all.length(); ++i) {
            JSONObject json = all.getJSONObject(i);
            Element element = elements.get(json.getString("id"));

            element.id = json.getString("id");

            if (json.has("weight")) {
                element.weight = json.getInt("weight");
            }
            if (json.has("location")) {
                elements.get(json.getString("location")).elements.add(element);
            }

            if (json.has("hidden")) {
                final JSONObject hidden = json.getJSONObject("hidden");
                for (String action : hidden.keySet()) {
                    element.hiddenElements.put(Word.Action.valueOf(action), elements.get(hidden.getString(action)));
                }
            }
        }

        // NPC
        for (int i = 0; i < npcs.length(); i++) {
            JSONObject npc_json = npcs.getJSONObject(i);
            NPC npc_element = (NPC) elements.get(npc_json.getString("id"));

            // no wearables

            if (npc_json.has("stoneAt"))
                npc_element.stoneAt = npc_json.getString("stoneAt");
        }

        // Item
        for (int i = 0; i < items.length(); i++) {
            JSONObject item_json = items.getJSONObject(i);
            Item item_element = (Item) elements.get(item_json.getString("id"));

            if (item_json.has("openable")) {
                item_element.openable = Item.OPENABLE.valueOf(item_json.getString("openable"));
            }
            if (item_json.has("lockedWith")) {
                item_element.lockedWith = elements.get(item_json.getString("lockedWith"));
            }
        }

        // Location
        for (int i = 0; i < locations.length(); i++) {
            JSONObject location_json = locations.getJSONObject(i);
            Location location_element = (Location) elements.get(location_json.getString("id"));

            final JSONObject exits = location_json.getJSONObject("exits");
            for (String direction : exits.keySet()) {
                JSONObject locationItem = exits.getJSONObject(direction);
                Location location = (Location) elements.get(locationItem.getString("location"));
                Item item = locationItem.has("item")
                        ? (Item) elements.get(locationItem.getString("item"))
                        : null;

                location_element.exits.put(
                        Word.Direction.valueOf(direction),
                        Utils.Pair.of(location, item)
                );
            }
        }


        return new HashSet<>(elements.values());
    }
}

package com.unizar.generic;

import com.unizar.Utils;
import com.unizar.game.Game;
import com.unizar.game.World;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

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

    private final Element winItem;
    private final Element winLocation;

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
            assertKey(npc, "id");
            assertKey(npc, "name");
            final String id = npc.getString("id");
            if (id.contains("Player"))
                elements.put(id, new Player(npc.getString("name")) {
                });
            else elements.put(id, new NPC(npc.getString("name")) {
            });
        }
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            assertKey(item, "id");
            assertKey(item, "name");
            elements.put(item.getString("id"), new Item(item.getString("name")) {
            });
        }
        for (int i = 0; i < locations.length(); i++) {
            JSONObject location = locations.getJSONObject(i);
            assertKey(location, "id");
            assertKey(location, "name");
            elements.put(location.getString("id"), new Location(
                    location.getString("name"),
                    location.optString("image", null),
                    location.optString("music", null)
            ) {
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
            Element element = getElement(json, "id", elements);

            element.id = json.getString("id");

            if (json.has("weight")) {
                element.weight = json.getInt("weight");
            }
            if (json.has("location")) {
                getElement(json, "location", elements).elements.add(element);
            }

            if (json.has("hidden")) {
                final JSONObject hidden = json.getJSONObject("hidden");
                for (String action : hidden.keySet()) {
                    element.hiddenElements.put(Word.Action.valueOf(action), getElement(hidden, action, elements));
                }
            }
        }

        // NPC
        for (int i = 0; i < npcs.length(); i++) {
            JSONObject npc_json = npcs.getJSONObject(i);
            NPC npc_element = (NPC) getElement(npc_json, "id", elements);

            if (npc_json.has("allowedLocations")) {
                npc_element.navigateLocations = getElements(npc_json.getJSONArray("allowedLocations"), elements);
                npc_element.navigateLocationsAreForbidden = false;
            }
            if (npc_json.has("forbiddenLocations")) {
                npc_element.navigateLocations = getElements(npc_json.getJSONArray("forbiddenLocations"), elements);
                npc_element.navigateLocationsAreForbidden = true;
            }

            if (npc_json.has("strenght")) {
                npc_element.strenght = npc_json.getInt("strenght");
            }
            if (npc_json.has("health")) {
                npc_element.health = npc_json.getInt("health");
            }
            if (npc_json.has("languages")) {
                final JSONArray array = npc_json.getJSONArray("languages");
                for (int j = 0; j < array.length(); j++) {
                    npc_element.languages.add(array.getString(j));
                }
            }

            if (npc_json.has("canFollowOrders")) {
                npc_element.canFollowOrders = npc_json.getBoolean("canFollowOrders");
            }
            if (npc_json.has("sleepAt")) {
                npc_element.sleepAt = npc_json.getString("sleepAt");
            }
            if (npc_json.has("attackItems")) {
                npc_element.attackItems = getElements(npc_json.getJSONArray("attackItems"), elements);
                npc_element.attackWeight = 1;
            }
            if (npc_json.has("pacificTurns")) {
                npc_element.pacificTurns = npc_json.getInt("pacificTurns");
            }
            if (npc_json.has("followNPCs")) {
                npc_element.followNPCs = getElements(npc_json.getJSONArray("followNPCs"), elements);
                npc_element.followWeight = 1;
            }
            if (npc_json.has("allies")) {
                npc_element.allies = getElements(npc_json.getJSONArray("allies"), elements);
            }
            if (npc_json.has("moveNPCsTo")) {
                npc_element.moveNPCsTo = getElement(npc_json, "moveNPCsTo", elements);
            }
            if (npc_json.has("talkPlayer")) {
                final JSONArray talks = npc_json.getJSONArray("talkPlayer");
                for (int j = 0; j < talks.length(); j++) {
                    JSONObject talk = talks.getJSONObject(j);
                    int turns = talk.optInt("turns", 1);
                    String sentence = talk.getString("sentence");
                    npc_element.talkPlayer.add(Utils.Pair.of(turns, sentence));
                }
                npc_element.talkWeight = 1;
            }
            if (npc_json.has("giveItems")) {
                npc_element.giveItems = getElements(npc_json.getJSONArray("giveItems"), elements);
                npc_element.giveWeight = 1;
            }

            if (npc_json.has("attackWeight")) {
                npc_element.attackWeight = npc_json.getInt("attackWeight");
            }
            if (npc_json.has("followWeight")) {
                npc_element.followWeight = npc_json.getInt("followWeight");
            }
            if (npc_json.has("navigateWeight")) {
                npc_element.navigateWeight = npc_json.getInt("navigateWeight");
            }
            if (npc_json.has("talkWeight")) {
                npc_element.talkWeight = npc_json.getInt("talkWeight");
            }
            if (npc_json.has("giveWeight")) {
                npc_element.giveWeight = npc_json.getInt("giveWeight");
            }
            if (npc_json.has("openWeight")) {
                npc_element.openWeight = npc_json.getInt("openWeight");
            }
            if (npc_json.has("pickWeight")) {
                npc_element.pickWeight = npc_json.getInt("pickWeight");
            }


        }

        // Item
        for (int i = 0; i < items.length(); i++) {
            JSONObject item_json = items.getJSONObject(i);
            Item item_element = (Item) getElement(item_json, "id", elements);

            if (item_json.has("openable")) {
                item_element.openable = Item.OPENABLE.valueOf(item_json.getString("openable"));
            }
            if (item_json.has("lockedWith")) {
                item_element.lockedWith = getElement(item_json, "lockedWith", elements);
            }
            if (item_json.has("language")) {
                item_element.language = item_json.getString("language");
            }
            if (item_json.has("description")) {
                item_element.description = item_json.getString("description");
            }
            if (item_json.has("makesInvisible")) {
                item_element.makesInvisible = item_json.getBoolean("makesInvisible");
            }
        }

        // Location
        for (int i = 0; i < locations.length(); i++) {
            JSONObject location_json = locations.getJSONObject(i);
            Location location_element = (Location) getElement(location_json, "id", elements);

            if (location_json.has("exits")) {
                final JSONObject exits = location_json.getJSONObject("exits");
                for (String direction : exits.keySet()) {
                    JSONObject locationItem = exits.getJSONObject(direction);
                    Location location = locationItem.has("location")
                            ? (Location) getElement(locationItem, "location", elements)
                            : null;
                    Item item = locationItem.has("item")
                            ? (Item) getElement(locationItem, "item", elements)
                            : null;

                    location_element.exits.put(
                            Word.Direction.valueOf(direction),
                            Utils.Pair.of(location, item)
                    );
                }
            }
            if (location_json.has("description")) {
                location_element.description = location_json.getString("description");
            }
        }


        return new HashSet<>(elements.values());
    }

    private static Set<Element> getElements(JSONArray array, Map<String, Element> elements) {
        Set<Element> set = new HashSet<>();
        array.forEach(npc -> {
            final String id = npc.toString();
            if (!elements.containsKey(id)) {
                throw new NoSuchElementException("There is no element with id '" + id + "'");
            }
            set.add(elements.get(id));
        });
        return set;
    }

    private static Element getElement(JSONObject object, String key, Map<String, Element> elements) {
        assertKey(object, key);

        final String id = object.getString(key);
        if (!elements.containsKey(id)) {
            throw new NoSuchElementException("There is no element with id '" + id + "'");
        }
        return elements.get(id);
    }

    private static void assertKey(JSONObject object, String property) {
        if (!object.has(property))
            throw new MissingResourceException("Missing property '" + property + "' in object " + object, "", "");
    }
}

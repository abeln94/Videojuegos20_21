package com.unizar.generic;

import com.unizar.Utils;
import com.unizar.game.Properties;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataLoader {

    public static final String FILE_NPCS = "/npcs.json";
    public static final String FILE_ITEMS = "/items.json";
    public static final String FILE_LOCATIONS = "/locations.json";
    private static final String FILE_PROPERTIES = "/properties.json";

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

//    public static void saveElements(String path, Set<Element> elements) throws IOException {
//
//
//        JSONArray npcs = new JSONArray();
//        JSONArray items = new JSONArray();
//        JSONArray locations = new JSONArray();
//
//        for (Element element : elements) {
//            JSONObject object = new JSONObject();
//
//            object.put("id", element.getId());
//            object.put("name", element.name);
//            if (element.weight != Integer.MAX_VALUE)
//                object.put("weight", element.weight);
//            Element e_location = element.getLocation();
//            if (e_location != null)
//                object.put("location", e_location.getId());
//
//            final JSONObject hiddenElements = new JSONObject();
//            element.hiddenElements.forEach((action, hidden) -> hiddenElements.put(action.name(), hidden.getId()));
//            if (hiddenElements.length() > 0) object.put("hidden", hiddenElements);
//
//            if (element instanceof NPC) {
//                NPC npc = (NPC) element;
//
//                npcs.put(object);
//            }
//            if (element instanceof Item) {
//                Item item = (Item) element;
//
//                Item.OPENABLE openable = item.openable;
//                if (openable != null)
//                    object.put("openable", openable);
//
//                Element lockedWith = item.lockedWith;
//                if (lockedWith != null)
//                    object.put("lockedWith", lockedWith.getId());
//
//                items.put(object);
//            }
//            if (element instanceof Location) {
//                Location location = (Location) element;
//
//                object.put("image", location.getImage());
//                object.put("music", location.music);
//
//                final JSONObject object_locations = new JSONObject();
//                location.exits.forEach(((direction, locationItemPair) -> {
//                    final JSONObject exits = new JSONObject();
//                    exits.put("location", locationItemPair.first.getId());
//                    if (locationItemPair.second != null)
//                        exits.put("item", locationItemPair.second.getId());
//                    object_locations.put(direction.name(), exits);
//                }));
//                object.put("exits", object_locations);
//
//                locations.put(object);
//            }
//        }
//
//        File folder = new File(path);
//        folder.mkdirs();
//
//        save(folder, FILE_NPCS, npcs);
//        save(folder, FILE_ITEMS, items);
//        save(folder, FILE_LOCATIONS, locations);
//    }
//
//    private static void save(File folder, String filename, JSONArray jsonArray) throws IOException {
//        File file = new File(folder, filename);
//        file.createNewFile();
//        FileWriter writer = new FileWriter(file);
//        writer.write(jsonArray.toString(2));
//        writer.close();
//    }

    // ------------------------- properties -------------------------

    public static Properties loadProperties(String root) throws IOException {
        final JSONObject properties = new JSONObject(Utils.readFile(root + FILE_PROPERTIES));

        return new Properties() {
            @Override
            public String getTitle() {
                return properties.getString("title");
            }

            @Override
            public int getImageRatio() {
                return properties.getInt("imageRatio");
            }

            @Override
            public String getImagePath(String label) {
                return root + properties.getString("imagePath").replace("{}", label);
            }

            @Override
            public String getMusicPath(String label) {
                return root + properties.getString("musicPath").replace("{}", label);
            }

            @Override
            public String getFontFile() {
                return root + properties.getString("fontFile");
            }

            @Override
            public String getStartScreen() {
                return properties.getString("startScreen");
            }

            @Override
            public String getWinScreen() {
                return properties.getString("winScreen");
            }

            @Override
            public String getStartDescription() {
                return properties.getString("startDescription");
            }

            @Override
            public String getWinDescription() {
                return properties.getString("winDescription");
            }

            @Override
            public String getHelpPath() {
                return root + properties.getString("help");
            }
        };

    }

    public static void saveProperties(String path, Properties properties) throws IOException {
        final JSONObject json_properties = new JSONObject();
        json_properties.put("title", properties.getTitle());
        json_properties.put("imageRatio", properties.getImageRatio());
        json_properties.put("imagePath", properties.getImagePath("{}"));
        json_properties.put("musicPath", properties.getMusicPath("{}"));
        json_properties.put("fontFile", properties.getFontFile());
        json_properties.put("startScreen", properties.getStartScreen());
        json_properties.put("winScreen", properties.getWinScreen());
        json_properties.put("startDescription", properties.getStartDescription());
        json_properties.put("winDescription", properties.getWinDescription());

        File file = new File(path, FILE_PROPERTIES);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(json_properties.toString(2));
        writer.close();
    }
}

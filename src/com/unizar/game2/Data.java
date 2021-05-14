package com.unizar.game2;

import com.unizar.Utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Set;

public class Data {

    // ------------------------- variables -------------------------
    private final String root;

    // ------------------------- data -------------------------

    private JSONObject data;

    // ------------------------- constructor -------------------------

    public Data(String root) {
        this.root = root;
    }

    public void loadFromFiles() {
        try {
            JSONParser parser = new JSONParser();

            for (String name : new String[]{"properties", "items", "locations", "npcs", "commands"}) {
                data.put(name, parser.parse(Utils.readFile(root + "/" + name + ".json")));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // ------------------------- serialization -------------------------

    public String serialize() {
        return data.toJSONString();
    }

    public void deserialize(String serialization) {
        try {
            this.data = (JSONObject) new JSONParser().parse(serialization);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // ------------------------- getters -------------------------

    public Set<String> getElementWords() {
        return null;
    }

    public Set<String> getActionWords() {
    }

    public Set<String> getModifierWords() {
    }

    public JSONObject getPlayer() {
        return null;
    }
}

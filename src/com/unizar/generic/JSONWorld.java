package com.unizar.generic;

import com.unizar.Utils;
import com.unizar.game.Game;
import com.unizar.game.World;
import com.unizar.game.elements.Element;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Look! I can use JSON!
 */
public class JSONWorld extends World {

    private Element winItem;
    private Element winLocation;

    public JSONWorld(String root) {
        try {
            // properties
            final JSONObject json_properties = new JSONObject(Utils.readFile(root + DataLoader.FILE_PROPERTIES));
            this.properties = new JSONProperties(root, json_properties);

            // elements
            elements = DataLoader.loadElements(root);

            winItem = elements.stream().filter(e -> e.id.equals(json_properties.getString("winItem"))).findFirst().orElse(null);
            winLocation = elements.stream().filter(e -> e.id.equals(json_properties.getString("winLocation"))).findFirst().orElse(null);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean playerWon(Game game) {
        return winLocation.elements.contains(winItem);
    }
}

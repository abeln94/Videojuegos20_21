package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.SpiderWeb;

public class GreenForest extends Location {

    public GreenForest() {
        super("en un bosque verde", "EastBlackRiver", "forest");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(EastBlackRiver.class), null));
        exits.put(Word.Direction.NORTHEAST, Utils.Pair.of(game.findElementByClassName(PlaceOfBlackSpider.class), game.findElementByClassName(SpiderWeb.class)));
        super.init();
    }
}

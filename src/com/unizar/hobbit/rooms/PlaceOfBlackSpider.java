package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.SpiderWeb;

public class PlaceOfBlackSpider extends Location {

    public PlaceOfBlackSpider() {
        super("en la guarida de las arañas", "PlaceBlackSpider");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTHEAST, Utils.Pair.of(game.findElementByClassName(ElvishClearing.class), game.findElementByClassName(SpiderWeb.class)));
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(GreenForest.class), game.findElementByClassName(SpiderWeb.class)));
        super.init();
    }
}

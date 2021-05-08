package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.MagicDoor;
import com.unizar.hobbit.items.SpiderWeb;

public class ElvishClearing extends Location {

    public ElvishClearing() {
        super("en un claro élfico con desniveles en el terreno y troncos", "ElvishClearing", "forest");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.NORTHEAST, Utils.Pair.of(game.findElementByClassName(Elvenkings.class), game.findElementByClassName(MagicDoor.class))); //TODO: hay que esperar a que los elfos abran la puerta
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(PlaceOfBlackSpider.class), game.findElementByClassName(SpiderWeb.class)));
        super.init();
    }
}

package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class BleakBarrenLand extends Location {

    public BleakBarrenLand() {
        super(" en una tierra desolada y yerma que alguna vez fue verde. ", "BleakBarrenLand", "wind");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.DOWN, Utils.Pair.of(game.findElementByClassName(StrongRiver.class), null));
        exits.put(Word.Direction.NORTH, Utils.Pair.of(game.findElementByClassName(RuinsTownDale.class), null));
        super.init();
    }
    //TODO: una vez se ha matado a Smaug el mensaje debería cambiar a "La desolación de Smaug"
}

package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;

public class MistyValley_1 extends Location {

    public MistyValley_1() {
        super("en un profundo valle con resto de niebla", "MistyValley", "iceCavern");
    }

    @Override
    public void init() {
        exits.put(Word.Direction.UP, Utils.Pair.of(game.findElementByClassName(SteepPath_3.class),null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(MistyValley_2.class),null));
        super.init();
    }
}
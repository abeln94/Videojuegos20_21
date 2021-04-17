package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GoldenKey;
import com.unizar.hobbit.items.StartChest;

public class MistyValley_2 extends Location {

    public MistyValley_2() {
        super("en un profundo valle con resto de niebla", "MistyValley");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(GoldenKey.class));

        exits.put(Word.Direction.UP, Utils.Pair.of(game.findElementByClassName(NarrowPath_2.class),null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(MistyValley_1.class),null));
        super.init();
    }
}
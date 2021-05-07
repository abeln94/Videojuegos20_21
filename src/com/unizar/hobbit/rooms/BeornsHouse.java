package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.Cupboard;

public class BeornsHouse extends Location {

    public BeornsHouse() {
        super("en la casa de Beorn", "BeornsHouse", "fire");
    }

    @Override
    public void init() {

        elements.add(game.findElementByClassName(Cupboard.class));

        exits.put(Word.Direction.NORTHEAST, Utils.Pair.of(game.findElementByClassName(Mirkwood.class), null));
        exits.put(Word.Direction.NORTHWEST, Utils.Pair.of(game.findElementByClassName(TrelessOpening.class), null));
        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(ForestGate.class), null));
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(NarrowDangerousPath.class), null));
        super.init();
    }
}

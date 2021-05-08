package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.RedDoor;

public class RedDoorRoom extends Location {

    public RedDoorRoom() {
        super("una pequeña sala con más barriles de vino", "CellarOfWine", "calm");
    }

    @Override
    public void init() {
        //TODO: aqui teletransporta el Butler a los secuestrados -> Thorin o Galdalf
        exits.put(Word.Direction.SOUTHWEST, Utils.Pair.of(game.findElementByClassName(CellarOfWine.class), game.findElementByClassName(RedDoor.class)));
        super.init();
    }
}

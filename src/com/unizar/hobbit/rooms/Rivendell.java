package com.unizar.hobbit.rooms;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Location;
import com.unizar.hobbit.items.GreenDoor;
import com.unizar.hobbit.items.StartChest;
import com.unizar.hobbit.npcs.Bilbo_Player;
import com.unizar.hobbit.npcs.Gandalf;
import com.unizar.hobbit.npcs.Thorin;

public class Rivendell extends Location {

    public Rivendell() {
        super("en Rivendell", "Rivendell");
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Elrond.class));

        exits.put(Word.Direction.EAST, Utils.Pair.of(game.findElementByClassName(DangerousPath.class), null));
        exits.put(Word.Direction.WEST, Utils.Pair.of(game.findElementByClassName(EmptyLand.class), null));
        super.init();
    }
}
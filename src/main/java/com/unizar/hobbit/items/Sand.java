package com.unizar.hobbit.items;

import com.unizar.game.commands.Word;
import com.unizar.game.elements.Item;

public class Sand extends Item {
    public Sand() {
        super("algo de arena");
    }

    @Override
    public void init() {
        super.init();
        hiddenElements.put(Word.Action.DIG, game.findElementByClassName(TrapDoor.class));
    }
}

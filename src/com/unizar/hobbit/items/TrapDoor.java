package com.unizar.hobbit.items;

import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Item;

public class TrapDoor extends Item {
    public TrapDoor() {
        super("una trampilla");
    }

    @Override
    public void init() {
        hiddenElements.put(Word.Action.BREAK, game.findElementByClassName(SmallKey.class));
        super.init();
    }
}

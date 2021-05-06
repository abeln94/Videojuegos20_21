package com.unizar.hobbit.items;

import com.unizar.game.commands.Word;
import com.unizar.game.elements.Item;

public class SpiderWeb extends Item {
    public SpiderWeb() {
        super("una tela de araña");
    }

    @Override
    public void init() {
        openable = OPENABLE.LOCKED; // can not be unlocked
        hiddenElements.put(Word.Action.BREAK, game.findElementByClassName(SpiderWeb_Broken.class));
        super.init();
    }
}

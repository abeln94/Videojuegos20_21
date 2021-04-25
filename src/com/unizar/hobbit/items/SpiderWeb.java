package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class SpiderWeb extends Item {
    public SpiderWeb() {
        super("una tela de araña");
    }

    @Override
    public void init() {
        openable = OPENABLE.CLOSED; // can not be unlocked
        super.init();
    }
}

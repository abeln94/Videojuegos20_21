package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class Food extends Item {
    public Food() {
        super("comida");
        weight = 1;
    }

    static public class ElrondFood extends Food {
    }

    static public class CupboardFood extends Food {
    }
}

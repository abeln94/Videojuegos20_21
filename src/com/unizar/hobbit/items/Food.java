package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class Food extends Item {
    public Food() {
        super("comida");
        weight = 1;
        alive = false; // will be 'created' by elrond
    }
}

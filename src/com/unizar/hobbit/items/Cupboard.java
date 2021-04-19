package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class Cupboard extends Item {
    public Cupboard() {
        super("un gran armario.");
    }

    @Override
    public void init() {
        opened = false;
        super.init();
    }

    @Override
    public void act() {
        if(opened){
            elements.add(game.findElementByClassName(Food.class));
        }
        else{
            elements.remove(game.findElementByClassName(Food.class));
        }
    }
}

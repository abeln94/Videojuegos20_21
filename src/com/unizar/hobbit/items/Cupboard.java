package com.unizar.hobbit.items;

import com.unizar.game.elements.Item;

public class Cupboard extends Item {
    public Cupboard() {
        super("Una pesada cortina. Tras ella una pared. En la pared hay un gran armario.");
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

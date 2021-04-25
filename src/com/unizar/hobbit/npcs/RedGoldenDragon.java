package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;

public class RedGoldenDragon extends NPC {

    public RedGoldenDragon() {
        super("El dorado dragón rojo");
        weight = 50;
    }

    @Override
    public void init() {
        super.init();
    }

    //TODO: se mueve aleatoriamente entre las tres direcciones
    //TODO: solo te ataca si entras al hall
    //TODO: solo se le puede matar con el arco y la flecha
    //TODO: al morir no desaparece y queda como el dragón muerto
}

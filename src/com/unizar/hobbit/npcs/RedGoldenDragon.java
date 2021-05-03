package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;

import java.util.ArrayList;

public class RedGoldenDragon extends NPC {

    public RedGoldenDragon() {
        super("El dorado dragón rojo");
        weight = 200;
        id = 7;
        lastAttackedBy = null;
        autonomo = true;
        inmortal = false;
        puedeDormir = false;
        puedeTP = false;
        puedeLeer = false;
        puedeMatarAJugador = true;
        lugares = new ArrayList<>();
        dormido = false;
        primerEncuentroJugador = false;
        saludos = new ArrayList<>();
        frases = new ArrayList<>();
        sitioTP = null;
        elementoAbrir = null;
        elementoLeer = null;
        arma = "fuego";
        orden = null;
    }

    @Override
    public void init() { super.init(); }

    @Override
    public void act() {
        super.act();
    }
}

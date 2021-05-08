package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;

public class Elrond extends NPC {

    private boolean playerSaw = false;

    public Elrond() {
        super("Elrond");
        weight = 100;
        id = 3;
/*        lastAttackedBy = null;
        autonomo = false;
        inmortal = true;
        puedeDormir = false;
        puedeTP = false;
        puedeLeer = true;
        puedeMatarAJugador = true;
        lugares = new ArrayList<>();
        dormido = false;
        primerEncuentroJugador = true;
        sitioTP = null;
        elementoAbrir = null;
        //elementoLeer = Map.class; TODO:
        arma = "espada";
        orden = null;
        saludos.add("Hola");
        frases.add("Bienvenido de nuevo a estas tierras");
        frases.add("Grata es su visita");*/
    }

    @Override
    public void init() { super.init(); }

    @Override
    public void act() {
        super.act();
    }
}
package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;

public class NastyGoblin extends NPC {

    public NastyGoblin() {
        super("Un goblin repugnante");
        weight = 40;
        id = 8;
        lastAttackedBy = null;
 /*       autonomo = false;
        inmortal = false;
        puedeDormir = false;
        puedeTP = true;
        puedeLeer = false;
        puedeMatarAJugador = false;
        lugares = new ArrayList<>();
        dormido = false;
        primerEncuentroJugador = false;
        saludos = new ArrayList<>();
        frases = new ArrayList<>();
        //sitioTP = GoblinDungeon.class; TODO:
        elementoAbrir = null;
        elementoLeer = null;
        arma = null;
        orden = null;*/
    }

    @Override
    public void init() { super.init(); }

    @Override
    public void act() {
        super.act();
    }
}

package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Bow;

public class Butler extends NPC {

    private boolean playerSaw = false;

    public Butler() {
        super("El mayordomo");
        weight = 40;
        id = 2;
/*        lastAttackedBy = null;
        autonomo = false;
        inmortal = false;
        puedeDormir = true;
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
        arma = "espada";
        orden = null;*/
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Bow.class));
        super.init();
    }

    @Override
    public void act() {
        super.act();
    }
}

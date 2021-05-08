package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Map;

public class Gandalf extends NPC {
    public Gandalf() {
        super("Gandalf");
        weight = 100;
        id = 4;
/*        lastAttackedBy = null;
        autonomo = true;
        inmortal = true;
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
        //elementoAbrir = GreenDoor.class;
        elementoLeer = null;
        arma = "baston";
        orden = null;*/
    }

    @Override
    public void init() {
        elements.add(game.findElementByClassName(Map.class));
        super.init();
    }

    @Override
    public void act() {
        super.act();
    }
}

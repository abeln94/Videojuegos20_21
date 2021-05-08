package com.unizar.hobbit.npcs;

import com.unizar.game.elements.NPC;

public class ViciousTroll extends NPC {

    private boolean playerSaw = false;

    public ViciousTroll() {
        super("Un Troll pendenciero");
        weight = 50;
        id = 20;
        lastAttackedBy = null;
  /*      autonomo = false;
        inmortal = false;
        puedeDormir = false;
        puedeTP = false;
        puedeLeer = false;
        puedeMatarAJugador = false;
        lugares = new ArrayList<>();
        dormido = false;
        primerEncuentroJugador = false;
        sitioTP = null;
        elementoAbrir = null;
        elementoLeer = null;
        arma = "piedra";
        orden = null;
        saludos.add("Mirah so hobis");
        frases.add("Me lo voi a hamar");
        frases.add("Eeeeeeeh miniyo, qeres una mini empanadilla?");*/
    }

    @Override
    public void init() { super.init(); }

    @Override
    public void act() {
        super.act();
    }
}

package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Result;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.rooms.GoblinDungeon;

import java.util.ArrayList;

public class Gollum extends NPC {

    public Gollum() {
        super("Gollum");
        weight = 40;
        id = 5;
        lastAttackedBy = null;
        autonomo = false;
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
        arma = "puños";
        orden = null;
    }

    @Override
    public void init() { super.init(); }

    @Override
    public void act() {
        super.act();
    }
}

package com.unizar.hobbit.npcs;

import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.LargeKey;

import java.util.ArrayList;

public class HideousTroll extends NPC {

    private boolean playerSaw = false;

    public HideousTroll() {
        super("Un Troll horrendo");
        weight = 50;
        id = 21;
        lastAttackedBy = null;
        autonomo = false;
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
        saludos.add("Pixa, pue son bien canijo");
        frases.add("E vito peazo de maíz en mi mierdah ma grande que este tío!");
        frases.add("Qero su bebeee.... asao, asao, asao, asao con ensalaaada, su bebe, su bebe, su bebe o me cago aqui joer");

    }

    @Override
    public void init() { super.init(); }

    @Override
    public void act() {
        super.act();
    }
}

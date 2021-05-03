package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.Bow;
import com.unizar.hobbit.items.Food;
import com.unizar.hobbit.items.Map;

import java.util.ArrayList;

public class Elrond extends NPC {

    private boolean playerSaw = false;

    public Elrond() {
        super("Elrond");
        weight = 100;
        id = 3;
        lastAttackedBy = null;
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
        frases.add("Grata es su visita");
    }

    @Override
    public void init() { super.init(); }

    @Override
    public void act() {
        super.act();
    }
}
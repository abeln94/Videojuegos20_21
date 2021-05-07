package com.unizar.hobbit.npcs;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.NPC;
import com.unizar.hobbit.items.GreenDoor;
import com.unizar.hobbit.items.Map;

import java.util.ArrayList;

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
        //elementoAbrir = GreenDoor.class; //TODO:
        elementoLeer = null;
        arma = "baston";
        orden = null;*/
    }

    @Override
    public void init() { super.init(); }

    @Override
    public void act() {
        super.act();
    }
}

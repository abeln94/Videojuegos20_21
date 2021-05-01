package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A generic NPC
 */
abstract public class NPC extends Element {

    /**
     * List of parameters that de machine use to select the next action of the npc
     */
    public NPC lastAttackedBy = null;
    public boolean autonomo = false;
    public boolean inmortal = false;
    public boolean puedeDormir = false;
    public boolean puedeTP = false;
    public boolean puedeLeer = false;
    public boolean puedeMatarAJugador = false;
    public List<Element> lugares = new ArrayList<>();
    public boolean dormido = false;
    public boolean primerEncuentroJugador = false;
    Map<String, Boolean> frases = new HashMap<>();
    public int cansancio = 0;
    public Element sitioTP = null;
    public Element elementoAbrir = null;
    public Element elementoLeer = null;
    public String arma = null;
    public String orden = null;
    public Element posicion = null;
    //inventario de npc son sus elements

    public NPC(String name) {
        super(name);
    }

    @Override
    public String getDescription(NPC npc) {
        String prefix = ": " + " Lleva";

        return this + Utils.joinList("", prefix, prefix, elements.stream().map(e -> e.getDescription(npc)).collect(Collectors.toList()));
    }

    /**
     * When this npc hears something
     *
     * @param message what was heard
     */
    public void hear(String message) {
        System.out.println("[" + this + "]> " + message);
    }


    /**
     * When another npc asks something to this npc (a command to execute)
     *
     * @param npc     who asked
     * @param message what was asked
     */
    public void ask(NPC npc, String message) {

        // parse message
        orden = message;
        /*Command parse = game.parser.parse(message);
        if (parse.parseError) {
            // bad command
            npc.hear(this + " te responde: Como has dicho?");
            return;
        }
        if (Utils.random.nextBoolean()) {
            // it was valid but...bad luck
            npc.hear("A " + this + " no le apetece.");
            return;
        }
        // execute
        final Result result = game.engine.execute(this, parse);
        if (!result.done) {
            npc.hear(this + " te responde: No puedo hacer eso");
        } else {
            hear(result.output);
        }*/
    }

    @Override
    public void act() {
        //
        Result result = game.engine.execute(this, Command.simple(Word.Action.WAIT));
        System.out.println(this + ": " + result);
    }

    @Override
    public void init() {
        super.init();
    }
}

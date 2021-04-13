package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;

import java.util.stream.Collectors;

/**
 * A generic NPC
 */
abstract public class NPC extends Element {

    /**
     * The location of this npc (duplicated entry to avoid searching)
     * (this is an element because you can be inside an item)
     */
    public Element location;

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
        Command parse = game.parser.parse(message);
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
        }
    }

    @Override
    public void act() {
        Result result = game.engine.execute(this, Command.simple(Word.Action.WAIT));
        System.out.println(this + ": " + result);
    }

    @Override
    public void init() {
        // register this NPC in the location room
        location.elements.add(this);

        super.init();
    }
}

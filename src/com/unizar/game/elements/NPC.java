package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.EngineException;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;

/**
 * A generic NPC
 */
abstract public class NPC extends Element {

    /**
     * The last npc that attacked this one
     */
    public NPC lastAttackedBy = null;

    public NPC(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return name + describeContents(".", ". Lleva:");
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

        try {
            // parse message
            Command parse = Command.parse(message, game.world.elements);

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
            }

        } catch (EngineException e) {
            // bad command
            npc.hear(this + " te responde: Como has dicho?");
        }
    }

    @Override
    public void act() {
        Result result = game.engine.execute(this, Command.simple(Word.Action.WAIT));
        System.out.println(this + ": " + result);
    }

    @Override
    public void init() {
        super.init();
    }
}

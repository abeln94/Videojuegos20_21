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
     * The location of this npc
     * (this is an element because you can be inside an item)
     */
    public Element location;

    public NPC(String name) {
        super(name);
    }

    @Override
    public String getDescription(NPC npc) {
        String prefix = ": " + this + " lleva";

        return super.getDescription(npc) + Utils.joinList("", prefix, prefix, elements.stream().map(e -> e.getDescription(npc)).collect(Collectors.toList()));
    }

    /**
     * When this npc hears something
     *
     * @param message what was heard
     */
    public void onHear(String message) {
        System.out.println(this + ": " + message);
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

package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A generic NPC
 */
abstract public class NPC extends Element {

    public Element location; // an element because you can be inside an item

    public Set<Element> elements = new HashSet<>();

    public NPC(String name) {
        super(name);
    }

    @Override
    public String getDescription(NPC npc) {
        String prefix = ": " + this + " lleva ";

        return super.getDescription(npc) + Utils.generateList("", prefix, prefix, elements.stream().map(e -> e.getDescription(npc)).collect(Collectors.toList()));
    }

    public void onHear(String message) {
        System.out.println(this + ": " + message);
    }

    @Override
    public void act() {
        Result result = game.engine.applyCommand(this, Command.simple(Word.Action.WAIT));
        System.out.println(this + ": " + result);
    }

    @Override
    public void init() {
        super.init();

        // register this NPC in the location room
        location.elements.add(this);
    }
}

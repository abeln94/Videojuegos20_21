package com.unizar.game.commands;

import com.unizar.game.elements.Element;

/**
 * A command to process.
 * Currently consist of an adverb + action + preposition + direction + element (any can be null)
 */
public class Command {
    public Word.Adverbs adverb;
    public Word.Action action;
    public Word.Preposition preposition;
    public Word.Direction direction;
    public Element element;

    public Command(Word.Adverbs adverb, Word.Action action, Word.Preposition preposition, Word.Direction direction, Element element) {
        this.adverb = adverb;
        this.action = action;
        this.preposition = preposition;
        this.direction = direction;
        this.element = element;
    }

    public static Command simple(Word.Action action) {
        return new Command(null, action, null, null, null);
    }

    public static Command act(Word.Action action, Element element) {
        return new Command(null, action, null, null, element);
    }

    public static Command go(Word.Direction direction) {
        return new Command(null, Word.Action.GO, null, direction, null);
    }

    @Override
    public String toString() {
        return (adverb == null ? "" : adverb + " - ")
                + action
                + (preposition == null ? "" : " - " + preposition)
                + (direction == null ? "" : " - " + direction)
                + (element == null ? "" : " - " + element);
    }
}

package com.unizar.game.commands;

import com.unizar.game.elements.Element;

public class Command {
    public Word.Adverbs adverb = null;
    public Word.Action action = null;
    public Word.Preposition preposition = null;
    public Word.Direction direction = null;
    public Element element = null;

    public Command() {
    }

    public Command(Word.Adverbs adverb, Word.Action action, Word.Preposition preposition, Word.Direction direction, Element element) {
        this.adverb = adverb;
        this.action = action;
        this.preposition = preposition;
        this.direction = direction;
        this.element = element;
    }

    public static Command simple(Word.Action action) {
        Command command = new Command();
        command.action = action;
        return command;
    }

    public static Command act(Word.Action action, Element element) {
        Command command = new Command();
        command.action = action;
        command.element = element;
        return command;
    }

    public static Command go(Word.Direction direction) {
        Command command = new Command();
        command.action = Word.Action.GO;
        command.direction = direction;
        return command;
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

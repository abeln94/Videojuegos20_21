package com.unizar.game.commands;

import com.unizar.game.elements.Element;

import java.util.Set;

/**
 * A processed command, contains the command elements for the engine
 */
public class Command {
    public Word.Modifier modifier;
    public Word.Action action;
    public Word.Direction direction;

    public String sequence;

    public FilterableElements main;
    public FilterableElements secondary;


    public String invalidToken = null;
    public boolean parseError = false;

    public Command beforeCommand = null;

    // ------------------------- constructors -------------------------

    public Command(Word.Modifier modifier, Word.Action action, Word.Direction direction, String sequence, FilterableElements main, FilterableElements secondary) {
        this.modifier = modifier;
        this.action = action;
        this.direction = direction;
        this.sequence = sequence;
        this.main = main;
        this.secondary = secondary;
    }

    /**
     * Create a new empty command
     *
     * @param elements base list of elements for both main and secondary
     */
    public Command(Set<Element> elements) {
        this(null, null, null, null, new FilterableElements(elements), new FilterableElements(elements));
    }

    /**
     * A simple action without parameters
     */
    public static Command simple(Word.Action action) {
        return new Command(null, action, null, null, null, null);
    }

    /**
     * An action that requires an element
     */
    public static Command act(Word.Action action, Element element) {
        return new Command(null, action, null, null, new FilterableElements(element), null);
    }

    /**
     * An action that requires two elements
     */
    public static Command act(Word.Action action, Element mainElement, Element secondaryElement) {
        return new Command(null, action, null, null, new FilterableElements(mainElement), new FilterableElements(secondaryElement));
    }

    /**
     * A go action
     */
    public static Command go(Word.Direction direction) {
        return new Command(null, Word.Action.GO, direction, null, null, null);
    }

    // ------------------------- generation -------------------------

    /**
     * Prepares this command after parsing
     */
    public void validate() {

        // special shortcuts
        if (action == null && direction != null) {
            // a direction without action is a go
            action = Word.Action.GO;
        }

        // chech merge
        if (beforeCommand != null) {
            // validate first the subcommand if necessary
            beforeCommand.validate();

            // merge with the subcommand values
            if (modifier == null) modifier = beforeCommand.modifier;
            if (action == null) action = beforeCommand.action;
        }

    }

    // ------------------------- filters -------------------------

    @Override
    public String toString() {
        return (modifier == null ? "" : modifier + " - ")
                + action
                + (direction == null ? "" : " - " + direction)
                + (main == null ? "" : " - " + main)
                + (secondary == null ? "" : " - " + secondary);
    }
}

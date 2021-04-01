package com.unizar.game.commands;

import com.unizar.game.elements.Element;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    // ------------------------- filters -------------------------

    /**
     * A collection of elements that can be filtered to match what the user expected
     */
    public static class FilterableElements {
        public Set<Element> elements;
        public String description = "";
        public String error = null;

        /**
         * Contains just one element
         *
         * @param element this element
         */
        public FilterableElements(Element element) {
            this.elements = Set.of(element);
            this.description = element.name;
        }

        /**
         * Contains a full list of elements
         *
         * @param elements the elements to initialize to
         */
        public FilterableElements(Set<Element> elements) {
            this.elements = elements;
        }

        /**
         * Adds a word description, filters the internal list
         *
         * @param word the word that the elements must match
         */
        public void addDescriptionWord(String word) {
            elements = elements.stream().filter(e -> Word.matchSentences(e.name, word)).collect(Collectors.toSet());
            description = description.isEmpty() ? word : description + " " + word;
        }

        /**
         * Makes the element require something.
         *
         * @param filter        the filter that should be true for all elements in this collection
         * @param message       the error message if, after applying the filter, no more elements are in the collection. Must contain a '{}' that will be replaced with the element description
         * @param noDescription if no description was provided when this command was generated, return this as the description
         * @return the error message, or null if this still contains elements after the filtering
         */
        public Command.FilterableElements require(Predicate<Element> filter, String message, String noDescription) {
            assert !elements.isEmpty();

            // already an error
            if (error != null) return this;

            // prepare the missed error string
            String missed = elements.size() == 1 ? elements.iterator().next().name // there is one element, use that one
                    : description.isEmpty() ? noDescription // there is no description, use the parameter
                    : "'" + description + "'"; // there is a description, use that

            // filter
            elements = elements.stream().filter(filter).collect(Collectors.toSet());

            // check if there are no more elements
            if (elements.isEmpty()) {
                error = message.replace("{}", missed);
            }

            // finished
            return this;
        }

        /**
         * Apply a function to the element, or returns an error
         *
         * @param moreNeeded string to return if there are multiple elements available
         * @param action     what to run if there is exactly one element
         * @return the corresponding result
         */
        public Result apply(String moreNeeded, Function<Element, Result> action) {
            return apply(moreNeeded, null, action);
        }

        /**
         * Apply a function to the element, or returns an error
         *
         * @param moreNeeded           string to return if there are multiple elements available
         * @param moreNeededAppendable appendable string for the moreNeeded result
         * @param action               what to run if there is exactly one element
         * @return the corresponding result
         */
        public Result apply(String moreNeeded, String moreNeededAppendable, Function<Element, Result> action) {
            switch (elements.size()) {
                case 1 -> {
                    // only one element, apply
                    return action.apply(elements.iterator().next());
                }
                case 0 -> {
                    // no elements, return previous error
                    assert error != null;
                    return Result.error(error);
                }
                default -> {
                    // multiple elements, more info needed
                    return Result.moreNeeded(moreNeeded, moreNeededAppendable);
                }
            }
        }

        @Override
        public String toString() {
            return (description.isEmpty() ? "" : "'" + description + "' ") + "(" + elements.size() + ") ";
        }
    }

    @Override
    public String toString() {
        return (modifier == null ? "" : modifier + " - ")
                + action
                + (direction == null ? "" : " - " + direction)
                + (main == null ? "" : " - " + main)
                + (secondary == null ? "" : " - " + secondary);
    }
}

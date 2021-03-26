package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Element;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A processed command, contains the command elements for the engine
 */
public class Command {
    public Word.Modifier modifier = null;
    public Word.Action action = null;
    public Word.Direction direction = null;

    public FilterableElements main;
    public FilterableElements secondary;


    public String invalidToken = null;
    public boolean parseError = false;

    // ------------------------- constructors -------------------------

    public Command(Word.Modifier modifier, Word.Action action, Word.Direction direction, FilterableElements main, FilterableElements secondary) {
        this.modifier = modifier;
        this.action = action;
        this.direction = direction;
        this.main = main;
        this.secondary = secondary;
    }

    /**
     * A simple action without parameters
     */
    public static Command simple(Word.Action action) {
        return new Command(null, action, null, null, null);
    }

    /**
     * An action that requires an element
     */
    public static Command act(Word.Action action, Element element) {
        return new Command(null, action, null, new FilterableElements(element), null);
    }

    /**
     * An action that requires two elements
     */
    public static Command act(Word.Action action, Element mainElement, Element secondaryElement) {
        return new Command(null, action, null, new FilterableElements(mainElement), new FilterableElements(secondaryElement));
    }

    /**
     * A go action
     */
    public static Command go(Word.Direction direction) {
        return new Command(null, Word.Action.GO, direction, null, null);
    }

    /**
     * Parse the sentence
     *
     * @param sentence user input
     * @param elements game elements
     */
    public Command(String sentence, Set<Element> elements) {
        List<String> words = Word.separateWords(sentence);

        main = new FilterableElements(elements);
        secondary = new FilterableElements(elements);

        boolean isSecondElement = false;
        for (String word : words) {
            if (word.isEmpty()) continue;
            Utils.Pair<Word.Type, Word.Token> parsing = Word.parse(word, elements);
            switch (parsing.first) {
                case ACTION -> {
                    if (action != null) {
                        parseError = true;
                        return;
                    }
                    action = (Word.Action) parsing.second;
                }
                case DIRECTION -> {
                    if (direction != null) {
                        parseError = true;
                        return;
                    }
                    direction = (Word.Direction) parsing.second;
                }
                case MODIFIER -> {
                    if (modifier != null) {
                        parseError = true;
                        return;
                    }
                    modifier = (Word.Modifier) parsing.second;
                }
                case PREPOSITION -> {
                    // next element will be the second element
                    isSecondElement = true;
                }
                case ELEMENT -> {
                    if (isSecondElement) secondary.addDescriptionWord(word);
                    else main.addDescriptionWord(word);
                }
                case MULTIPLE -> {
                    parseError = true;
                    return;
                }
                case UNKNOWN -> {
                    invalidToken = word;
                    parseError = true;
                    return;
                }
            }
        }

        // special shortcuts
        if (action == null && direction != null) {
            // a direction without action is a go
            action = Word.Action.GO;
        }

    }

    // ------------------------- filters -------------------------

    /**
     * A collection of elements that can be filtered to match what the user expected
     */
    public static class FilterableElements {
        public Set<Element> elements;
        public String description = "";

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
        public String require(Predicate<Element> filter, String message, String noDescription) {
            assert !elements.isEmpty();

            // prepare the missed error string
            String missed = elements.size() == 1 ? elements.iterator().next().name // there is one element, use that one
                    : description.isEmpty() ? noDescription // there is no description, use the parameter
                    : "'" + description + "'"; // there is a description, use that

            // filter
            elements = elements.stream().filter(filter).collect(Collectors.toSet());

            // check if there are no more elements
            if (elements.isEmpty()) {
                return message.replace("{}", missed);
            }

            // all ok
            return null;
        }

        /**
         * @return the element, only if there is one left
         */
        public Element get() {
            assert elements.size() > 0;
            return elements.size() == 1 ? elements.iterator().next() : null;
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

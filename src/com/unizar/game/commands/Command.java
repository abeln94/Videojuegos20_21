package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Element;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A command to process.
 * Currently consist of an adverb + action + preposition + direction + element (any can be null)
 */
public class Command {
    public Word.Modifier modifier;
    public Word.Action action;
    public Word.Direction direction;

    public Set<Element> mainElement;
    public Set<Element> secondaryElement;

    public String mainElementDescription;
    private String secondaryElementDescription;


    public String invalidToken = null;
    public boolean parseError = false;

    // ------------------------- constructors -------------------------

    public Command(Word.Modifier modifier, Word.Action action, Word.Direction direction, Element mainElement, Element secondaryElement) {
        this.modifier = modifier;
        this.action = action;
        this.direction = direction;
        this.mainElement = mainElement == null ? Collections.emptySet() : Set.of(mainElement);
        this.secondaryElement = secondaryElement == null ? Collections.emptySet() : Set.of(secondaryElement);
        this.mainElementDescription = mainElement == null ? "" : mainElement.name;
        this.secondaryElementDescription = secondaryElement == null ? "" : secondaryElement.name;
    }

    public static Command simple(Word.Action action) {
        return new Command(null, action, null, null, null);
    }

    public static Command act(Word.Action action, Element element) {
        return new Command(null, action, null, element, null);
    }

    public static Command go(Word.Direction direction) {
        return new Command(null, Word.Action.GO, direction, null, null);
    }

    public Command(String sentence, Word.Token[] elementTokens, Set<Element> elements) {
        List<String> words = Word.separateWords(sentence);

        mainElement = elements;
        secondaryElement = elements;

        boolean isSecondElement = false;
        for (String word : words) {
            if (word.isEmpty()) continue;
            Utils.Pair<Word.Type, Word.Token> parsing = Word.parse(word, elementTokens);
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
                    if (isSecondElement) {
                        secondaryElementDescription = secondaryElementDescription == null ? word : secondaryElementDescription + word;
                        filterSecondaryElement(e -> Word.matchSentences(e.name, word));
                    } else {
                        mainElementDescription = mainElementDescription == null ? word : mainElementDescription + word;
                        filterMainElement(e -> Word.matchSentences(e.name, word));
                    }
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
    }

    // ------------------------- filters -------------------------

    public void reFilterMainElement(List<Predicate<Element>> filters) {
        mainElement = reFilter(filters, mainElement);
    }

    public void reFilterSecondaryElement(List<Predicate<Element>> filter) {
        secondaryElement = reFilter(filter, secondaryElement);
    }

    public void filterMainElement(Predicate<Element> filter) {
        mainElement = filter(filter, mainElement);
    }

    public void filterSecondaryElement(Predicate<Element> filter) {
        secondaryElement = filter(filter, secondaryElement);
    }

    private Set<Element> reFilter(List<Predicate<Element>> filters, Set<Element> elements) {
        for (Predicate<Element> filter : filters) {
            if (elements.size() <= 1) break;
            elements = filter(filter, elements);
        }
        return elements;
    }

    private Set<Element> filter(Predicate<Element> filter, Set<Element> elements) {
        return elements.stream().filter(filter).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return (modifier == null ? "" : modifier + " - ")
                + action
                + (direction == null ? "" : " - " + direction)
                + (mainElement == null ? "" : " - " + mainElement.size())
                + (secondaryElement == null ? "" : " - " + secondaryElement.size());
    }
}

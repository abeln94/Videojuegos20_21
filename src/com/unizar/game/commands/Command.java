package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Element;

import java.util.ArrayList;
import java.util.List;
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

    public Element element;
    public Element secondElement;
    public List<String> elementDescription = new ArrayList<>();
    private List<String> secondElementDescription = new ArrayList<>();


    public String invalidToken = null;
    public boolean parseError = false;

    // ------------------------- constructors -------------------------

    public Command(Word.Modifier modifier, Word.Action action, Word.Direction direction, Element elementDescription, Element secondElementDescription) {
        this.modifier = modifier;
        this.action = action;
        this.direction = direction;
        this.elementDescription = elementDescription == null ? null : Word.separateWords(elementDescription.name);
        this.secondElementDescription = secondElementDescription == null ? null : Word.separateWords(secondElementDescription.name);
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

    public Command(String sentence, Word.Token[] elements) {
        List<String> words = Word.separateWords(sentence);

        boolean isSecondElement = false;

        for (String word : words) {
            if (word.isEmpty()) continue;
            Utils.Pair<Word.Type, Object> parsing = Word.parse(word, elements);
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
                    if (isSecondElement) secondElementDescription.add(word);
                    else elementDescription.add(word);
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

    public void filterElement(List<Element> interactable, Predicate<Element> valid) {
        element = filter(interactable, valid, elementDescription);
    }

    public void filterSecondaryElement(List<Element> interactable, Predicate<Element> valid) {
        secondElement = filter(interactable, valid, secondElementDescription);
    }

    private Element filter(List<Element> interactable, Predicate<Element> valid, List<String> descriptions) {

        // try first to find a valid elements
        List<Element> filteredElements = interactable.stream().filter(element -> valid.test(element) && Word.match(descriptions, element.name)
        ).collect(Collectors.toList());

        if (filteredElements.size() == 1) {
            return filteredElements.get(0);
        }

        // if not, try to find an interactable one
        filteredElements = interactable.stream().filter(element -> Word.match(descriptions, element.name)
        ).collect(Collectors.toList());

        if (filteredElements.size() == 1) {
            return filteredElements.get(0);
        }

        return null;
    }

    @Override
    public String toString() {
        return (modifier == null ? "" : modifier + " - ")
                + action
                + (direction == null ? "" : " - " + direction)
                + (elementDescription == null ? "" : " - " + elementDescription)
                + (secondElementDescription == null ? "" : " - " + secondElementDescription);
    }
}

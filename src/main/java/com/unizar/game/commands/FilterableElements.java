package com.unizar.game.commands;

import com.unizar.game.elements.Element;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A collection of elements that can be filtered to match what the user expected
 */
public class FilterableElements {
    public Set<Element> elements;
    public String description = "";
    public String error = null;
    public boolean all = false;

    // ------------------------- construction -------------------------

    /**
     * Contains just one element
     *
     * @param element this element
     */
    public FilterableElements(Element element) {
        this.elements = Collections.singleton(element);
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

    public void markAsAll() {
        all = true;
    }

    // ------------------------- using -------------------------

    /**
     * Makes the element require something.
     *
     * @param filter        the filter that should be true for all elements in this collection
     * @param message       the error message if, after applying the filter, no more elements are in the collection. Must contain a '{}' that will be replaced with the element description
     * @param noDescription if no description was provided when this command was generated, return this as the description
     * @return the error message, or null if this still contains elements after the filtering
     */
    public FilterableElements require(Predicate<Element> filter, String message, String noDescription) {
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
            case 1:
                // only one element, apply
                return action.apply(elements.iterator().next());
            case 0:
                // no elements, return previous error
                assert error != null;
                return Result.error(error);
            default:
                // multiple elements
                if (all) {
                    // apply to all
                    // TODO: in different turns
                    Result result = new Result();
                    for (Element element : elements) {
                        result.merge(action.apply(element));
                        if (!result.done) {
                            return result;
                        }
                    }
                    return result;
                } else {
                    //more info needed
                    return Result.moreNeeded(moreNeeded, moreNeededAppendable);
                }
        }
    }

    @Override
    public String toString() {
        return (description.isEmpty() ? "" : "'" + description + "' ") + "(" + elements.size() + ") ";
    }
}

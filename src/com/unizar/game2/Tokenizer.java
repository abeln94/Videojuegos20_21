package com.unizar.game2;

import com.unizar.Utils;

import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Tokenizer {

    /**
     * The 'all' words
     */
    static String all = "todo toda todos todas";

    /**
     * The 'any' words
     */
    static String any = "algo alguna alguno cualquier cualquiera";

    /**
     * A type of word
     */
    public enum Type {
        ACTION,
        PARAMETER,
        ELEMENT,
        UNKNOWN,
        IGNORE,
        ALL,
        ANY,
        AND,
    }

    /**
     * Ignorable words in the input string
     */
    static String ignorable = "el la los las un una unos unas";

    // ------------------------- tokenizer -------------------------

    /**
     * Separates a sentence into words
     *
     * @param sentence sentence to separate
     * @return list of words
     */
    static public List<String> separateWords(String sentence) {

        // spanish is difficult
        sentence = sentence.replaceAll("\\bal\\b", "a el");
        sentence = sentence.replaceAll("\\bdel\\b", "de el");

        return Arrays.stream(sentence.toLowerCase().split(" +")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    // ------------------------- parsing -------------------------

    /**
     * Returns the token (and its type) of this word
     *
     * @param word     the word to test
     * @param elements list of game elements
     * @return a pair type-object (object depends on the type)
     */
    static public Utils.Pair<Type, String> parse(String word, Set<String> elements, Set<String> actions, Set<String> parameters) {

        // check ignorable
        if (matchSentences(ignorable, word)) return Utils.Pair.of(Type.IGNORE, null);

        // check external actions
        Optional<String> action = actions.stream().filter(a -> matchSentences(a, word)).findFirst();
        if (action.isPresent()) return Utils.Pair.of(Type.ACTION, action.get());

        // check parameters
        Optional<String> preposition = parameters.stream().filter(p -> matchSentences(p, word)).findFirst();
        if (preposition.isPresent()) return Utils.Pair.of(Type.PARAMETER, preposition.get());

        // all
        if (matchSentences(all, word)) return Utils.Pair.of(Type.ALL, null);

        // any
        if (matchSentences(any, word)) return Utils.Pair.of(Type.ANY, null);

        // check the word in the game-specific elements
        Optional<String> element = elements.stream().filter(p -> matchSentences(p, word)).findFirst();
        if (element.isPresent()) return Utils.Pair.of(Type.ELEMENT, element.get());


        // nothing matched, the word is unknown
        return Utils.Pair.of(Type.UNKNOWN, null);
    }

    // ------------------------- matching -------------------------

    /**
     * Returns true iff words matches
     *
     * @param baseWord from a game token/element
     * @param testWord from user input, what to check
     * @return true if they match, false otherwise
     */
    static public boolean matchWords(String baseWord, String testWord) {
        Collator collate = Collator.getInstance();
        collate.setStrength(java.text.Collator.PRIMARY);
        collate.setDecomposition(java.text.Collator.CANONICAL_DECOMPOSITION);
        return collate.equals(testWord.toLowerCase(), baseWord.toLowerCase());
    }

    /**
     * Returns true iff both sentence matches.
     *
     * @param baseSentence the name of the game element (not all words need to be in the testSentence)
     * @param testSentence the user input (all the words need to be in the baseSentence)
     * @return true iff they match
     */
    static public boolean matchSentences(String baseSentence, String testSentence) {
        List<String> testWords = separateWords(testSentence);
        List<String> baseWords = separateWords(baseSentence);

        // return true iff each word in the baseSentence matches with at least one from the baseSentence
        return testWords.stream().allMatch(testWord -> baseWords.stream().anyMatch(baseWord -> matchWords(baseWord, testWord)));
    }
}

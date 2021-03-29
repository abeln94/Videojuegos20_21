package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Container for all the engine words
 */
public class Word {

    public interface Token {
        String getName();
    }

    /**
     * A distinguishable action
     */
    public enum Action implements Token {
        BREAK("romper"),
        CLIMB("saltar"),
        CLOSE("cerrar"),
        CROSS("cruzar"),
        DIG("cavar"),
        DRINK("beber"),
        DROP("soltar"),
        EAT("comer"),
        EMPTY("vaciar"),
        ENTER("entrar"),
        FILL("rellenar"),
        FOLLOW("seguir"),
        GIVE("dar"),
        GO("ir"),
        KILL("matar"),
        LOCK("bloquear"),
        PICK("elegir"),
        PUT("poner"),
        OPEN("abrir"),
        RUN("correr"),
        SAY("decir"),
        SHOOT("disparar"),
        SWIM("nadar"),
        TAKE("coger"),
        THROW("lanzar"),
        TIE("atar"),
        TURN("girar"),
        UNLOCK("desbloquear"),
        UNTIE("desatar"),
        WEAR("vestir"),

        // special
        EXAMINE("examinar leer"),
        HELP("ayuda"),
        INVENTORY("inventario"),
        LOAD("cargar"),
        LOOK("mirar"),
        NOPRINT("noimprimir"),
        PAUSE("pausar"),
        PRINT("imprimir"),
        QUIT("salir"),
        SAVE("guardar"),
        SCORE("puntuación"),
        WAIT("esperar"),
        ;

        Action(String name) {
            this.name = name;
        }

        public final String name;

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * A specific direction where you can navigate to
     */
    public enum Direction implements Token {
        NORTH("el norte"),
        NORTHEAST("el noreste"),
        SOUTH("el sur"),
        NORTHWEST("el noroeste"),
        EAST("el este"),
        SOUTHEAST("el sureste"),
        WEST("el oeste"),
        SOUTHWEST("el sureste"),
        UP("arriba"),
        DOWN("abajo"),
        ;

        Direction(String name) {
            this.name = name;
        }

        public final String name;

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * A preposition for a secondary element
     */
    public enum Preposition implements Token {
        ACROSS("cruzando"),
        AT("a"),
        FROM("desde"),
        IN("en"),
        INTO("dentro"),
        OFF("lejos"),
        ON("sobre"),
        OUT("fuera"),
        THROUGH("través"),
        TO("hacia"),
        UP("hasta"),
        WITH("con"),
        ;

        Preposition(String name) {
            this.name = name;
        }

        public final String name;

        @Override
        public String getName() {
            return name;
        }
    }


    /**
     * A modifier
     */
    public enum Modifier implements Token {
        CAREFULLY("cuidadosamente"),
        SOFTLY("suavemente"),
        QUICKLY("rápidamente"),
        VICIOUSLY("viciosamente"),
        ;

        Modifier(String name) {
            this.name = name;
        }

        public final String name;

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * A type of word
     */
    public enum Type {
        ACTION,
        DIRECTION,
        PREPOSITION,
        MODIFIER,
        ELEMENT,
        MULTIPLE,
        UNKNOWN,
        IGNORE,
        ;
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

        sentence = " " + sentence + " ";

        // spanish is difficult
        sentence = sentence.replaceAll(" al ", " a el ");

        return Arrays.stream(sentence.toLowerCase().split(" +")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    // ------------------------- parsing -------------------------

    /**
     * Returns the token (and its type) of this word
     *
     * @param word     the word to test
     * @param elements list of game elements
     * @return a pair type-token (token is null when not needed)
     */
    static public Utils.Pair<Type, Token> parse(String word, Set<Element> elements) {

        // first check the word from all the game tokens
        if (Word.matchSentences(ignorable, word)) return Utils.Pair.of(Type.IGNORE, null);


        // create the list
        List<Utils.Pair<Type, Token>> all = new ArrayList<>();
        for (Utils.Pair<Type, ? extends Token[]> tokens : List.of(
                Utils.Pair.of(Type.ACTION, Word.Action.values()),
                Utils.Pair.of(Type.DIRECTION, Word.Direction.values()),
                Utils.Pair.of(Type.PREPOSITION, Word.Preposition.values()),
                Utils.Pair.of(Type.MODIFIER, Modifier.values())
        )) {
            all.addAll(Arrays.stream(tokens.second).map(t -> Utils.Pair.of(tokens.first, t)).collect(Collectors.toList()));
        }

        // filter
        List<Utils.Pair<Type, Token>> filtered = all.stream().filter(p -> Word.matchSentences(p.second.getName(), word)).collect(Collectors.toList());
        switch (filtered.size()) {
            case 0:
                // nothing, continue checking
                break;
            case 1:
                // found, return it
                return filtered.get(0);
            default:
                // multiple options
                System.out.println("multiple elements for '" + word + "': " + filtered);
                return Utils.Pair.of(Type.MULTIPLE, null);
        }

        // second check the word in the game-specific elements

        // filter
        Set<String> matchingTokens = elements.stream()
                .flatMap(element -> Word.separateWords(element.name).stream())
                .distinct()
                .filter(elementWord -> Word.matchWords(word, elementWord))
                .collect(Collectors.toSet());
        // check if there is a match
        if (matchingTokens.size() >= 1) return Utils.Pair.of(Type.ELEMENT, null);

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
        return testWord.equals(baseWord);
    }

    /**
     * Returns true iff both sentence matches.
     *
     * @param baseSentence the name of the game element (not all words need to be in the testSentence)
     * @param testSentence the user input (all the words need to be in the baseSentence)
     * @return true iff they match
     */
    static public boolean matchSentences(String baseSentence, String testSentence) {
        List<String> testWords = Word.separateWords(testSentence);
        List<String> baseWords = Word.separateWords(baseSentence);

        // return true iff each word in the baseSentence matches with at least one from the baseSentence
        return testWords.stream().allMatch(testWord -> baseWords.stream().anyMatch(baseWord -> Word.matchWords(baseWord, testWord)));
    }

}

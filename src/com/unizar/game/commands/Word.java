package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Element;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Container for all the engine words
 */
public class Word {

    /**
     * A distinguishable action
     */
    public enum Action {
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
        KILL("matar atacar"),
        LOCK("bloquear"),
        PICK("elegir coger"),
        PUT("poner colocar"),
        OPEN("abrir"),
        PULL("estirar"),
        RUN("correr"),
        SAY("decir pedir"),
        SHOOT("disparar"),
        SWIM("nadar"),
        //TAKE("coger"), same as PICK
        THROW("lanzar"),
        TIE("atar"),
        TURN("girar"),
        UNLOCK("desbloquear"),
        UNTIE("desatar"),
        WEAR("ponerse ponerte vestir"),
        UNWEAR("quitarse quitarte desvestir"), // the original is TAKE OFF

        // special
        EXAMINE("examinar leer"),
        HELP("ayuda"),
        //        INVENTORY("inventario"),
        LOAD("cargar"),
        LOOK("mirar"),
        NOPRINT("noimprimir"),
        //        PAUSE("pausar"),
        PRINT("imprimir"),
        QUIT("salir"),
        SAVE("guardar"),
        SCORE("puntuación"),
        WAIT("esperar"),

        // new
        SEARCH("buscar"),

        // spanishSpecial
        TIRAR("tirar"),
        ;

        Action(String alias) {
            this.alias = alias;
        }

        public final String alias;
    }

    /**
     * A specific direction where you can navigate to
     */
    public enum Direction {
        NORTH("el norte", "n"),
        NORTHEAST("el noreste", "ne"),
        SOUTH("el sur", "s"),
        NORTHWEST("el noroeste", "no"),
        EAST("el este", "e"),
        SOUTHEAST("el sureste", "se"),
        WEST("el oeste", "o w"),
        SOUTHWEST("el suroeste", "so"),
        UP("arriba", "ar"),
        DOWN("abajo", "ab"),
        ;

        Direction(String description, String extraAlias) {
            this.description = description;
            this.alias = description + " " + extraAlias;
        }

        public final String description;
        public final String alias;
    }

    /**
     * A preposition for a secondary element
     */
    public enum Preposition {
        ACROSS("cruzando"),
        AT("a"),
        FROM("desde de"),
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

        Preposition(String alias) {
            this.alias = alias;
        }

        public final String alias;
    }


    /**
     * A modifier
     */
    public enum Modifier {
        CAREFULLY("cuidadosamente"),
        SOFTLY("suavemente"),
        QUICKLY("rápidamente"),
        VICIOUSLY("viciosamente"),
        ;

        Modifier(String alias) {
            this.alias = alias;
        }

        public final String alias;
    }

    /**
     * The 'all' words
     */
    static String all = "todo toda todos todas";

    /**
     * The 'any' words
     */
    static String any = "algo alguna alguno cualquier cualquiera";

    /**
     * To separate commands
     */
    static String and = "y";

    /**
     * A type of word
     */
    public enum Type {
        ACTION,
        DIRECTION,
        PREPOSITION,
        MODIFIER,
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
    static public Utils.Pair<Type, Object> parse(String word, Set<Element> elements) {

        // first check ignorable
        if (Word.matchSentences(ignorable, word)) return Utils.Pair.of(Type.IGNORE, null);

        // then check actions
        Optional<Action> action = Arrays.stream(Action.values()).filter(a -> Word.matchSentences(a.alias, word)).findFirst();
        if (action.isPresent()) return Utils.Pair.of(Type.ACTION, action.get());

        // later directions
        Optional<Direction> direction = Arrays.stream(Direction.values()).filter(d -> Word.matchSentences(d.alias, word)).findFirst();
        if (direction.isPresent()) return Utils.Pair.of(Type.DIRECTION, direction.get());

        // next prepositions
        Optional<Preposition> preposition = Arrays.stream(Preposition.values()).filter(p -> Word.matchSentences(p.alias, word)).findFirst();
        if (preposition.isPresent()) return Utils.Pair.of(Type.PREPOSITION, preposition.get());

        // now modifiers
        final Optional<Modifier> modifier = Arrays.stream(Modifier.values()).filter(m -> Word.matchSentences(m.alias, word)).findFirst();
        if (modifier.isPresent()) return Utils.Pair.of(Type.MODIFIER, modifier.get());

        // the joiner
        if (Word.matchSentences(and, word)) return Utils.Pair.of(Type.AND, null);

        // all
        if (Word.matchSentences(all, word)) return Utils.Pair.of(Type.ALL, null);

        // any
        if (Word.matchSentences(any, word)) return Utils.Pair.of(Type.ANY, null);


        // and finally, check the word in the game-specific elements
        long matchingObjectTokens = elements.stream()
                .flatMap(element -> Word.separateWords(element.name).stream())
                .distinct()
                .filter(elementWord -> Word.matchWords(word, elementWord))
                .count();
        if (matchingObjectTokens >= 1) return Utils.Pair.of(Type.ELEMENT, null);


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

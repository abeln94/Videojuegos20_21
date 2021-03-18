package com.unizar.game.commands;

import com.unizar.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Container for all the engine words
 */
public class Word {

    public interface Token {
        String getName();
    }

    static public class ElementToken implements Token {
        String name;

        public ElementToken(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * An action
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
        EXAMINE("examinar"),
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
     * A direction where you can navigate to
     */
    public enum Direction implements Token {
        NORTH("norte"),
        NORTHEAST("noreste"),
        SOUTH("sur"),
        NORTHWEST("noroeste"),
        EAST("este"),
        SOUTHEAST("sureste"),
        WEST("oeste"),
        SOUTHWEST("sureste"),
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

    public enum Type {
        ACTION,
        DIRECTION,
        PREPOSITION,
        MODIFIER,
        ELEMENT,
        MULTIPLE,
        UNKNOWN,
        ;
    }

    // ------------------------- tokenizer -------------------------

    static public List<String> separateWords(String sentence) {
        return Arrays.asList(sentence.toLowerCase().split(" +"));
    }

    // ------------------------- parsing -------------------------

    static public Utils.Pair<Type, Object> parse(String word, Token[] elementWords) {
        for (Utils.Pair<Type, Token[]> tokens : new Utils.Pair[]{
                Utils.Pair.of(Type.ACTION, Word.Action.values()),
                Utils.Pair.of(Type.DIRECTION, Word.Direction.values()),
                Utils.Pair.of(Type.PREPOSITION, Word.Preposition.values()),
                Utils.Pair.of(Type.MODIFIER, Modifier.values()),
                Utils.Pair.of(Type.ELEMENT, elementWords),
        }) {
            List<Token> list = Arrays.stream(tokens.second).filter(w -> Word.match(word, w.getName())).collect(Collectors.toList());
            switch (list.size()) {
                case 0:
                    // nothing, continue
                    break;
                case 1:
                    // found, return
                    return Utils.Pair.of(tokens.first, list.get(0));
                default:
                    // multiple
                    return Utils.Pair.of(Type.MULTIPLE, null);
            }
        }

        return Utils.Pair.of(Type.UNKNOWN, null);
    }

    static public boolean match(String description, String word) {
        return word.startsWith(description);
    }

    static public boolean match(List<String> descriptions, String sentence) {
        List<String> words = Word.separateWords(sentence);

        return descriptions.stream().allMatch(description -> words.stream().anyMatch(word -> Word.match(description, word)));
    }

    // ------------------------- find -------------------------


    /**
     * Container for matching words
     */
    public static class Matches {
        List<Action> actions = new ArrayList<>();
        List<Modifier> modifiers = new ArrayList<>();
        List<Direction> directions = new ArrayList<>();
        List<Preposition> prepositions = new ArrayList<>();
    }

    /**
     * Returns all the matching words found in a sentence
     *
     * @param words list of words of a sentence
     * @return all the matching words (per block)
     */
    static public Matches getWords(List<String> words) {
        Matches matches = new Matches();
        matches.actions = Arrays.stream(Action.values()).filter(a -> words.contains(a.name)).collect(Collectors.toList());
        matches.modifiers = Arrays.stream(Modifier.values()).filter(a -> words.contains(a.name)).collect(Collectors.toList());
        matches.directions = Arrays.stream(Direction.values()).filter(a -> words.contains(a.name)).collect(Collectors.toList());
        matches.prepositions = Arrays.stream(Preposition.values()).filter(a -> words.contains(a.name)).collect(Collectors.toList());
        return matches;
    }

}

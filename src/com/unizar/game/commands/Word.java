package com.unizar.game.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Word {


    public enum Direction {
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
    }

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
    }

    public enum Preposition {
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
    }


    public enum Adverbs {
        CAREFULLY("cuidadosamente"),
        SOFTLY("suavemente"),
        QUICKLY("rápidamente"),
        VICIOUSLY("viciosamente"),
        ;

        Adverbs(String name) {
            this.name = name;
        }

        public final String name;
    }

    // ------------------------- find -------------------------

    public static class Matches {
        List<Action> actions = new ArrayList<>();
        List<Adverbs> adverbs = new ArrayList<>();
        List<Direction> directions = new ArrayList<>();
        List<Preposition> prepositions = new ArrayList<>();
    }

    static public Matches getWords(List<String> words) {
        Matches matches = new Matches();
        matches.actions = Arrays.stream(Action.values()).filter(a -> words.contains(a.name)).collect(Collectors.toList());
        matches.adverbs = Arrays.stream(Adverbs.values()).filter(a -> words.contains(a.name)).collect(Collectors.toList());
        matches.directions = Arrays.stream(Direction.values()).filter(a -> words.contains(a.name)).collect(Collectors.toList());
        matches.prepositions = Arrays.stream(Preposition.values()).filter(a -> words.contains(a.name)).collect(Collectors.toList());
        return matches;
    }

}

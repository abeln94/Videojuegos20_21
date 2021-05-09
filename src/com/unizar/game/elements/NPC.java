package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.EngineException;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A generic NPC
 */
abstract public class NPC extends Element {

    // ------------------------- non configurable properties -------------------------

    /**
     * The wearable elements
     */
    public final Set<Element> wearables = new HashSet<>();

    public int sawPlayer = 0;

    public NPC lastAttackedBy = null;

    // ------------------------- engine properties -------------------------

    public Set<Element> allowedLocations = null;
    public Set<Element> disallowedLocations = null;

    public int fuerza = 0; //como de fuerte pega el golpe. Para calcular golpe es fuerza + aleatorio d10 + bonificador arma
    public int constitucion = 0; //lo robusto del pj
    public int vida = 0; //, la vida es constitución + aleatorio dSegúnClase

    public Set<String> languages = null;

    // ------------------------- ia -------------------------

    public enum ACTION {
        ATACK_NPC,
        FOLLOW_PLAYER,
        GOTO,
        TALK,
        GIVE,
        OPEN,
        PICK,
    }

    // parameters
    public boolean canFollowOrders = false;
    public String sleepAt;
    public Set<Element> attackNPCs = null;
    public Set<Element> followNPCs = null;
    public Element moveNPCsTo = null;
    public Set<Utils.Pair<Integer, String>> talkPlayer = null;
    public Set<Element> giveItems = null;

    // weights
    public int attackWeight = 0;
    public int followWeight = 0;
    public int gotoWeight = 0;
    public int talkWeight = 0;
    public int giveWeight = 0;
    public int openWeight = 0;
    public int pickWeight = 0;


    // ------------------------- todo -------------------------

    public List<String> idiomas = new ArrayList<>(); //lista de idiomas que conoce, si un objeto está escrito en ese lo puede leer
    //inventario de npc son sus elements

    public NPC(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return name + (isSleep() ? " dormido" : "") + describeContents(".", ". Lleva:");
    }

    /**
     * When this npc hears something
     *
     * @param message what was heard
     */
    public void hear(String message) {
        System.out.println("[" + this + "]> " + message);
    }


    /**
     * When another npc asks something to this npc (a command to execute)
     *
     * @param npc     who asked
     * @param message what was asked
     */
    public void ask(NPC npc, String message) {
        if (!canFollowOrders) {
            npc.hear(npc + " no te hace caso");
            return;
        }

        // convert message
        // when you say 'darme el mapa' the 'me' part is replaced by the npc
        message = message.replaceAll("\\b([^ ]*)me\\b", "a " + npc.name + " $1");

        try {
            // execute
            Command parse = Command.parse(message, game.world.elements);
            Result result = game.engine.execute(this, parse);
            hear(result.output);
            if (!result.done) {
                npc.hear(this + " te responde: No se hacer eso");
            }
        } catch (EngineException e) {
            // bad command
            npc.hear(this + " te responde: Como has dicho?");
        }
    }

    public boolean isSleep() {
        if (sleepAt == null)
            return false;
        if (sleepAt.equalsIgnoreCase("night") && game.world.night)
            return true;
        if (sleepAt.equalsIgnoreCase("day") && !game.world.night)
            return true;

        return false;
    }

    @Override
    public void act() {
        // can't act if sleep
        if (isSleep()) return;

        // update talkedPlayer
        if (game.getPlayer().getLocation() == getLocation())
            sawPlayer++;
        else
            sawPlayer = 0;

        // return attack
        if (lastAttackedBy != null) {
            Result result = game.engine.execute(this, Command.act(Word.Action.KILL, lastAttackedBy));
            lastAttackedBy = null;
            if (result.done) {
                hear(result.output);
                return;
            }
        }

        // tp player
        if (moveNPCsTo != null) {
            getLocation().elements.stream().filter(e -> e instanceof NPC).forEach(npc -> {
                npc.moveTo(moveNPCsTo);
                npc.hear(this + " te lleva hasta " + moveNPCsTo + ".");
            });
        }

        int retry = 10;
        while (retry-- > 0) {
            Command possibleCommand = getPossibleCommand();
            if (possibleCommand == null) return;

            Result result = game.engine.execute(this, possibleCommand);
            if (result.done) {
                hear(result.output);
                return;
            }
        }

    }

    private Command getPossibleCommand() {

        Set<Utils.Pair<ACTION, Integer>> actions = new HashSet<>();
        actions.add(Utils.Pair.of(ACTION.ATACK_NPC, attackWeight));
        actions.add(Utils.Pair.of(ACTION.FOLLOW_PLAYER, followWeight));
        actions.add(Utils.Pair.of(ACTION.GOTO, gotoWeight));
        actions.add(Utils.Pair.of(ACTION.TALK, talkWeight));
        actions.add(Utils.Pair.of(ACTION.GIVE, giveWeight));
        actions.add(Utils.Pair.of(ACTION.OPEN, openWeight));
        actions.add(Utils.Pair.of(ACTION.PICK, pickWeight));
        ACTION action = Utils.pickWeightedRandom(actions);

        if (action == null) return null;

        switch (action) {
            case ATACK_NPC:
                if (attackNPCs.isEmpty()) return null;
                return Command.act(Word.Action.KILL, Utils.pickRandom(attackNPCs));

            case FOLLOW_PLAYER:
                if (followNPCs.isEmpty()) return null;
                return Command.act(Word.Action.FOLLOW, Utils.pickRandom(followNPCs));

            case GOTO:
                final Element location = getLocation();
                if (!(location instanceof Location)) return null;
                Word.Direction direction = Utils.pickRandom(((Location) location).exits.keySet());
                return Command.go(direction);

            case TALK:
                if (talkPlayer.isEmpty()) return null;
                // prepare sentences
                Set<String> sentences = talkPlayer.stream()
                        .filter(p -> p.first >= sawPlayer)
                        .map(p -> p.second)
                        .collect(Collectors.toSet());
                String sentence = Utils.pickRandom(sentences);
                if (sentence == null) return null;
                return Command.say(game.getPlayer(), sentence);

            case GIVE:
                if (giveItems.isEmpty()) return null;
                final Element item = Utils.pickRandom(giveItems);
                if (item.getLocation() == null) item.moveTo(this);
                return Command.act(Word.Action.GIVE, item);

            case OPEN:
                return Command.act(Word.Action.OPEN, Utils.pickRandom(getInteractable()));

            case PICK:
                return Command.act(Word.Action.PICK, Utils.pickRandom(getInteractable()));
        }
        return null;
    }
}

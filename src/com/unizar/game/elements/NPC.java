package com.unizar.game.elements;

import com.unizar.Utils;
import com.unizar.game.commands.Command;
import com.unizar.game.commands.EngineException;
import com.unizar.game.commands.Result;
import com.unizar.game.commands.Word;

import java.util.HashSet;
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

    public Set<Element> navigateLocations = null;
    public boolean specifiedLocationsAreForbidden = true;

    public int fuerza = 1; //como de fuerte pega el golpe. Para calcular golpe es fuerza + aleatorio d10 + bonificador arma
    public int constitucion = 0; //lo robusto del pj
    public int vida = 10; //, la vida es constitución + aleatorio dSegúnClase

    public Set<String> languages = null;

    // ------------------------- ia -------------------------

    public enum ACTION {
        ATACK_ITEM,
        ATACK_PLAYER,
        FOLLOW_NPCS,
        NAVIGATE,
        TALK,
        GIVE,
        OPEN,
        PICK,
    }

    // parameters
    public boolean canFollowOrders = false;
    public String sleepAt;
    public Set<Element> attackItems = null;
    public int pacificTurns = Integer.MAX_VALUE;
    public Set<Element> followNPCs = null;
    public Element moveNPCsTo = null;
    public Set<Utils.Pair<Integer, String>> talkPlayer = null;
    public Set<Element> giveItems = null;

    // weights
    public int attackWeight = 0;
    public int followWeight = 0;
    public int navigateWeight = 0;
    public int talkWeight = 0;
    public int giveWeight = 0;
    public int openWeight = 0;
    public int pickWeight = 0;

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
            getLocation().elements.stream()
                    .filter(e -> e instanceof NPC)
                    .filter(e -> e != this)
                    .collect(Collectors.toSet())
                    .forEach(npc -> {
                        npc.moveTo(moveNPCsTo);
                        npc.hear(this + " te lleva hasta " + moveNPCsTo + ".");
                    });
        }

        int retry = 10;
        while (retry-- > 0) {
            Command possibleCommand = getPossibleCommand();
            if (possibleCommand == null) continue;

            Result result = game.engine.execute(this, possibleCommand);
            if (result.done) {
                hear(result.output);
                break;
            }
        }

    }

    private Command getPossibleCommand() {

        Set<Utils.Pair<ACTION, Integer>> actions = new HashSet<>();
        actions.add(Utils.Pair.of(ACTION.ATACK_ITEM, attackWeight));
        actions.add(Utils.Pair.of(ACTION.ATACK_PLAYER, attackWeight));
        actions.add(Utils.Pair.of(ACTION.FOLLOW_NPCS, followWeight));
        actions.add(Utils.Pair.of(ACTION.NAVIGATE, navigateWeight));
        actions.add(Utils.Pair.of(ACTION.TALK, talkWeight));
        actions.add(Utils.Pair.of(ACTION.GIVE, giveWeight));
        actions.add(Utils.Pair.of(ACTION.OPEN, openWeight));
        actions.add(Utils.Pair.of(ACTION.PICK, pickWeight));
        ACTION action = Utils.pickWeightedRandom(actions);

        if (action == null) return null;

        switch (action) {
            case ATACK_ITEM:
                if (attackItems == null || attackItems.isEmpty()) return null;
                final Element attack = Utils.pickRandom(getInteractable().stream()
                        .filter(e -> e instanceof NPC)
                        .filter(e -> e.elements.stream().anyMatch(has -> attackItems.contains(has)))
                        .collect(Collectors.toSet()));
                if (attack == null) return null;
                return Command.act(Word.Action.KILL, attack);

            case ATACK_PLAYER:
                if (pacificTurns >= sawPlayer) return null;
                return Command.act(Word.Action.KILL, game.getPlayer());

            case FOLLOW_NPCS:
                Element npc;
                if (followNPCs == null) {
                    npc = Utils.pickRandom(game.findElementsByClassName(NPC.class));
                } else {
                    if (followNPCs.isEmpty()) return null;
                    npc = Utils.pickRandom(followNPCs);
                }
                if (npc.getLocation() == getLocation()) {
                    return Command.simple(Word.Action.WAIT);
                } else {
                    return Command.act(Word.Action.FOLLOW, npc);
                }

            case NAVIGATE:
                final Element location = getLocation();
                if (!(location instanceof Location)) return null;
                Word.Direction direction = Utils.pickRandom(((Location) location).exits.keySet());
                return Command.go(direction);

            case TALK:
                if (talkPlayer == null || talkPlayer.isEmpty()) return null;
                // prepare sentences
                Set<String> sentences = talkPlayer.stream()
                        .filter(p -> sawPlayer >= p.first)
                        .map(p -> p.second)
                        .collect(Collectors.toSet());
                String sentence = Utils.pickRandom(sentences);
                if (sentence == null) return null;
                return Command.say(game.getPlayer(), sentence);

            case GIVE:
                Element item;
                if (giveItems == null) {
                    item = Utils.pickRandom(elements);
                    if (item == null) return null;
                } else {
                    if (giveItems.isEmpty()) return null;
                    item = Utils.pickRandom(giveItems);
                }
                if (item.getLocation() == null) item.moveTo(this);
                return Command.act(Word.Action.GIVE, item, game.getPlayer());

            case OPEN:
                final Element interactable = Utils.pickRandom(getInteractable());
                if (interactable == null) return null;
                return Command.act(Word.Action.OPEN, interactable);

            case PICK:
                final Element interactablee = Utils.pickRandom(getInteractable());
                if (interactablee == null) return null;
                return Command.act(Word.Action.PICK, interactablee);
        }
        return null;
    }
}

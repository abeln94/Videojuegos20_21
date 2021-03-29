package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;

import java.util.List;
import java.util.Map;

/**
 * Executes a command from an npc.
 */
public class Engine {

    /**
     * Tries to execute the command from a specific npc
     *
     * @param npc     npc who initiated the command
     * @param command which command to process
     * @return A Result whether the command was applied, needs more info, or is wrong
     */
    public Result execute(NPC npc, Command command) {
        System.out.println(npc + " " + command);

        if (command.parseError) {
            if (command.invalidToken != null) {
                return Result.error("No entiendo '" + command.invalidToken + "'");
            } else {
                return Result.error("Como dices?");
            }
        }

        if (command.action == null) {
            return Result.error("Que quieres que haga?");
        }

        final List<Element> interactable = npc.location.getInteractable();
        interactable.remove(npc);

        switch (command.action) {
            case WAIT -> {
                return Result.done("Esperas. El tiempo pasa.");
            }
            case LOOK -> {
                return Result.error("[obsoleto: la descripción está ahora a la derecha]");
            }
            case SAVE -> {
                return Result.error("[obsoleto: pulsa F6 para guardar]");
            }
            case LOAD -> {
                return Result.error("[obsoleto: pulsa F9 para cargar]");
            }
            case OPEN -> {

                // you must see the element
                String error = command.main.require(
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                );
                if (error != null) return Result.error(error);

                // and it needs to be openable
                error = command.main.require(
                        e -> e instanceof Item && ((Item) e).opened != null,
                        "No puedo abrir {}.",
                        "nada"
                );
                if (error != null) return Result.error(error);

                // and be closed
                error = command.main.require(
                        e -> ((Item) e).opened == Boolean.FALSE,
                        "{} está ya abierto/a.",
                        "todo"
                );
                if (error != null) return Result.error(error);

                // got it?
                Item element = (Item) command.main.get();
                if (element == null) {
                    // multiple results
                    return Result.moreNeeded("Que quieres que abra?");
                }

                // open
                element.opened = true;
                npc.location.notifyNPCs(npc, npc + " abre " + element + ".");
                return Result.done("Abres " + element + ".");
            }
            case CLOSE -> {

                // you must see the element
                String error = command.main.require(
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                );
                if (error != null) return Result.error(error);

                // and it needs to be closeable
                error = command.main.require(
                        e -> e instanceof Item && ((Item) e).opened != null,
                        "No puedo cerrar {}.",
                        "nada"
                );
                if (error != null) return Result.error(error);

                // and be opened
                error = command.main.require(
                        e -> ((Item) e).opened == Boolean.TRUE,
                        "{} está ya cerrado/a.",
                        "todo"
                );
                if (error != null) return Result.error(error);

                // got it?
                Item element = (Item) command.main.get();
                if (element == null) {
                    // multiple results
                    return Result.moreNeeded("Que quieres que cierre?");
                }

                // close
                element.opened = false;
                npc.location.notifyNPCs(npc, npc + " cierra " + element + ".");
                return Result.done("Cierras " + element + ".");

            }
            case GO -> {
                if (command.direction == null) {
                    // no direction
                    // original game makes instead a 'go through'
                    return Result.moreNeeded("Hacia donde quieres que vaya?");
                }

                if (!(npc.location instanceof Location)) {
                    // inside an item
                    return Result.error("No puedes moverte mientras estás en " + npc.location + ".");
                }

                Utils.Pair<Location, Item> le = ((Location) npc.location).exits.get(command.direction);

                if (le == null) {
                    // no exit
                    return Result.error("No puedes ir hacia " + command.direction.description + ".");
                }

                Location newLocation = le.first;
                Item throughItem = le.second;

                if (throughItem != null && !throughItem.opened) {
                    // closed exit
                    return Result.error(throughItem + " está cerrado/a.");
                }

                // notify old npc
                npc.location.notifyNPCs(npc, npc + " va hacia " + command.direction.description + ".");

                // move
                npc.location.elements.remove(npc);
                npc.location = newLocation;
                npc.location.elements.add(npc);

                // notify new npc
                npc.location.notifyNPCs(npc, npc + " entra.");

                return Result.done("Te diriges hacia " + command.direction.description);
            }
            case FOLLOW -> {
                // check if we are inside something
                if (!(npc.location instanceof Location)) return Result.error("No puedes seguir a nadie desde aquí.");

                // the element must be an npc
                String error = command.main.require(
                        e -> e instanceof NPC,
                        "No puedes seguir a {}.",
                        "nadie"
                );
                if (error != null) return Result.error(error);

                // and not be interactable
                error = command.main.require(
                        e -> !interactable.contains(e),
                        "Ya estás con {}.",
                        "todos"
                );
                if (error != null) return Result.error(error);

                // and be in one of the connected exits
                error = command.main.require(
                        otherNPC -> ((Location) npc.location).exits.entrySet().stream().anyMatch(l -> l.getValue().first.elements.contains(otherNPC)),
                        "No veo a {}.",
                        "nadie a quien seguir"
                );
                if (error != null) return Result.error(error);

                // found it?
                NPC toFollow = (NPC) command.main.get();
                if (toFollow == null) {
                    // multiple results
                    return Result.moreNeeded("A quien quieres seguir?");
                }

                // find direction and follow
                for (Map.Entry<Word.Direction, Utils.Pair<Location, Item>> entry : ((Location) npc.location).exits.entrySet()) {
                    if (entry.getValue().first.elements.contains(toFollow)) {
                        // execute as a go command
                        Result result = execute(npc, Command.go(entry.getKey()));
                        if (result.done) {
                            // ok
                            return Result.done("Sigues a " + toFollow + ".");
                        } else {
                            // can't follow
                            return Result.error("No puedes seguir a " + toFollow + " desde aquí.");
                        }
                    }
                }

                throw new RuntimeException("Unexpected condition");
            }
            case GIVE -> {

                // we must have it
                String error = command.main.require(
                        npc.elements::contains,
                        "No tienes {}.",
                        "nada que dar"
                );
                if (error != null) return Result.error(error);

                // have it?
                Element elementToGive = command.main.get();
                if (elementToGive == null) {
                    // multiple results
                    return Result.moreNeeded("Que quieres dar?");
                }

                // check who to give it to

                // it must be an npc
                error = command.secondary.require(
                        e -> e instanceof NPC,
                        "No puedes darle cosas a {}.",
                        "nadie"
                );
                if (error != null) return Result.error(error);

                // and be interactable
                error = command.secondary.require(
                        interactable::contains,
                        "No veo a {}.",
                        "nadie desde aquí"
                );
                if (error != null) return Result.error(error);

                // found it?
                Element whoToGiveItTo = command.secondary.get();
                if (whoToGiveItTo == null) {
                    // multiple results
                    return Result.moreNeeded("A quién se lo quieres dar?", Word.Preposition.AT.alias);
                }

                // give
                npc.elements.remove(elementToGive);
                whoToGiveItTo.elements.add(elementToGive);
                whoToGiveItTo.hear(npc + " te da " + elementToGive + ".");
                return Result.done("Le das " + elementToGive + " a " + whoToGiveItTo + ".\n" + whoToGiveItTo + " te da las gracias.");
            }
            case EXAMINE -> {
                // you must see the element
                String error = command.main.require(
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                );
                if (error != null) return Result.error(error);

                // it must be an item
                error = command.main.require(
                        e -> e instanceof Item,
                        "No puedes examinar {}.",
                        "nada"
                );
                if (error != null) return Result.error(error);

                // found it?
                Item element = (Item) command.main.get();
                if (element == null) {
                    // multiple results
                    return Result.moreNeeded("Que quieres examinar?");
                }

                // examine
                return Result.done("Examinas " + element + ".\n" + element.examine(npc));
            }
            case SAY -> {

                // the element must be an npc
                String error = command.main.require(
                        e -> e instanceof NPC,
                        "No puedes hablarle a {}.",
                        "nadie"
                );
                if (error != null) return Result.error(error);

                // and be interactable
                error = command.main.require(
                        interactable::contains,
                        "No veo a {} por aquí.",
                        "nadie"
                );
                if (error != null) return Result.error(error);

                // found it?
                NPC toSay = (NPC) command.main.get();
                if (toSay == null) {
                    // multiple results
                    return Result.moreNeeded("A quien quieres hablarle?");
                }

                // check subsequence
                if (command.sequence == null) {
                    return Result.moreNeeded("Que quieres decirle?", "\"");
                }

                // ask to NPC
                toSay.ask(npc, command.sequence);
                return Result.done(""); // the say command already handled the output
            }
        }


        return Result.error("Aún no se hacer eso!");
    }
}

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
            case INVENTORY -> {
                return Result.error("[obsoleto: tu inventario se muestra ahora a la derecha]");
            }
            case SAVE -> {
                return Result.error("[obsoleto: pulsa F6 para guardar]");
            }
            case LOAD -> {
                return Result.error("[obsoleto: pulsa F9 para cargar]");
            }
            case OPEN -> {
                return command.main.require(
                        // you must see the element
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and it needs to be openable
                        e -> e instanceof Item && ((Item) e).opened != null,
                        "No puedes abrir {}.",
                        "nada"
                ).require(
                        // and be close
                        e -> ((Item) e).opened == Boolean.FALSE,
                        "{} está ya abierto/a.",
                        "todo"
                ).apply("Que quieres que abra?", element -> {

                    // open
                    ((Item) element).opened = true;
                    npc.location.notifyNPCs(npc, npc + " abre " + element + ".");
                    return Result.done("Abres " + element + ".");
                });
            }
            case CLOSE -> {
                return command.main.require(
                        // you must see the element
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and it needs to be closeable
                        e -> e instanceof Item && ((Item) e).opened != null,
                        "No puedes cerrar {}.",
                        "nada"
                ).require(
                        // and be open
                        e -> ((Item) e).opened == Boolean.TRUE,
                        "{} está ya cerrado/a.",
                        "todo"
                ).apply("Que quieres que cierre?", element -> {

                    // close
                    ((Item) element).opened = false;
                    npc.location.notifyNPCs(npc, npc + " cierra " + element + ".");
                    return Result.done("Cierras " + element + ".");
                });
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
                    return Result.error(throughItem + " está cerrado/a."); // TODO: add a 'genre' to items to remove the ugly 'o/a'
                }

                // notify old npc
                npc.location.notifyNPCs(npc, npc + " va hacia " + command.direction.description + ".");

                // move
                setParent(npc, npc.location, newLocation);

                // notify new npc
                npc.location.notifyNPCs(npc, npc + " entra.");

                return Result.done("Te diriges hacia " + command.direction.description);
            }
            case FOLLOW -> {
                // check if we are inside something
                if (!(npc.location instanceof Location)) return Result.error("No puedes seguir a nadie desde aquí.");

                return command.main.require(
                        // the element must be an npc
                        e -> e instanceof NPC,
                        "No puedes seguir a {}.",
                        "nadie"
                ).require(
                        // and not be interactable
                        e -> !interactable.contains(e),
                        "Ya estás con {}.",
                        "todos"
                ).require(
                        // and be in one of the connected exits
                        otherNPC -> ((Location) npc.location).exits.entrySet().stream().anyMatch(l -> l.getValue().first.elements.contains(otherNPC)),
                        "No veo a {}.",
                        "nadie a quien seguir"
                ).apply("A quien quieres seguir?", toFollow -> {

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
                });
            }
            case GIVE -> {
                return command.main.require(
                        // we must have it
                        npc.elements::contains,
                        "No tienes {}.",
                        "nada que dar"
                ).apply("Que quieres dar?", elementToGive -> {

                    // check who to give it to
                    return command.secondary.require(
                            // it must be an npc
                            e -> e instanceof NPC,
                            "No puedes darle cosas a {}.",
                            "nadie"
                    ).require(
                            // and be interactable
                            interactable::contains,
                            "No veo a {}.",
                            "nadie desde aquí"
                    ).apply("A quién se lo quieres dar?", Word.Preposition.AT.alias + " ", whoToGiveItTo -> {

                        // give
                        setParent(elementToGive, npc, whoToGiveItTo);
                        whoToGiveItTo.hear(npc + " te da " + elementToGive + ".");
                        return Result.done("Le das " + elementToGive + " a " + whoToGiveItTo + ".\n" + whoToGiveItTo + " te da las gracias.");
                    });
                });
            }
            case PICK -> {
                return command.main.require(
                        // it must be in the location of the npc
                        npc.location.elements::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // it must have less weight
                        e -> e.weight < npc.weight,
                        "No puedes coger {}.",
                        "nada"
                ).apply("Que quieres coger?", pickable -> {

                    // pick
                    setParent(pickable, npc.location, npc);
                    return Result.done("Coges " + pickable);
                });
            }
            case DROP -> {
                return command.main.require(
                        // You must have it
                        npc.elements::contains,
                        "No tienes {}.",
                        "nada"
                ).apply("Que quieres tirar?", dropable -> {

                    // drop
                    setParent(dropable, npc, npc.location);
                    return Result.done("Tiras " + dropable);
                });
            }
            case EXAMINE -> {
                return command.main.require(
                        // you must see the element
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // it must be an item
                        e -> e instanceof Item,
                        "No puedes examinar {}.",
                        "nada"
                ).apply("Que quieres examinar?", element -> {

                    // examine
                    return Result.done("Examinas " + element + ".\n" + ((Item) element).examine(npc));
                });

            }
            case SAY -> {
                return command.secondary.require(
                        // the element must be an npc
                        e -> e instanceof NPC,
                        "No puedes hablarle a {}.",
                        "nadie"
                ).require(
                        // and be interactable
                        interactable::contains,
                        "No veo a {} por aquí.",
                        "nadie"
                ).apply("A quien quieres hablarle?", toSay -> {

                    // check subsequence
                    if (command.sequence == null) {
                        return Result.moreNeeded("Que quieres decirle?", "\"");
                    }

                    // ask to NPC
                    ((NPC) toSay).ask(npc, command.sequence);
                    return Result.done(""); // the say command already handled the output
                });
            }
        }


        return Result.error("Aún no se hacer eso!");
    }

    // ------------------------- utils -------------------------

    /**
     * Sets the parent of an element (moves the element)
     *
     * @param element   what to move
     * @param oldParent from where
     * @param newParent to where
     */
    private void setParent(Element element, Element oldParent, Element newParent) {
        assert oldParent.elements.contains(element);
        oldParent.elements.remove(element);
        newParent.elements.add(element);
        if (element instanceof NPC) ((NPC) element).location = newParent;
    }

}

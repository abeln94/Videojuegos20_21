package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

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

        final List<Element> interactable = npc.getLocation().getInteractable();
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
            case QUIT -> {
                return Result.error("[obsoleto: pulsa la X de la ventana si quieres cerrar el juego]");
            }
            case OPEN -> {
                return command.main.require(
                        // you must see the element
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and it needs to be openable
                        e -> e instanceof Item && ((Item) e).openable != null,
                        "No puedes abrir {}.",
                        "nada"
                ).require(
                        // and not be open already
                        e -> ((Item) e).openable != Item.OPENABLE.OPENED,
                        "{} está ya abierto/a.",
                        "todo"
                ).require(
                        // and not be locked by an element
                        e -> ((Item) e).openable != Item.OPENABLE.LOCKED,
                        "{} está bloqueado/a.",
                        "todo"
                ).apply("Que quieres abrir?", element -> {
                    // open
                    ((Item) element).openable = Item.OPENABLE.OPENED;
                    npc.getLocation().notifyNPCs(npc, npc + " abre " + element + ".");
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
                        e -> e instanceof Item && ((Item) e).openable != null,
                        "No puedes cerrar {}.",
                        "nada"
                ).require(
                        // and not be closed
                        e -> ((Item) e).openable != Item.OPENABLE.CLOSED,
                        "{} está ya cerrado/a.",
                        "todo"
                ).require(
                        // nor locked
                        e -> ((Item) e).openable != Item.OPENABLE.LOCKED,
                        "{} está bloqueado/a.",
                        "todo"
                ).apply("Que quieres cerrar?", element -> {

                    // close
                    ((Item) element).openable = Item.OPENABLE.CLOSED;
                    npc.getLocation().notifyNPCs(npc, npc + " cierra " + element + ".");
                    return Result.done("Cierras " + element + ".");
                });
            }
            case UNLOCK -> {
                return command.main.require(
                        // you must see the element
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and it needs to be openable
                        e -> e instanceof Item && ((Item) e).openable != null && ((Item) e).lockedWith != null,
                        "No puedes desbloquear {}.",
                        "nada"
                ).require(
                        // and be locked
                        e -> ((Item) e).openable == Item.OPENABLE.LOCKED,
                        "{} está ya desbloqueado.",
                        "todo"
                ).apply("Que quieres desbloquear?", unlockable -> {

                    return command.secondary.require(
                            // we must have it
                            npc.elements::contains,
                            "No tienes {}.",
                            "nada con lo que desbloquearlo"
                    ).require(
                            // and be the unlockable item
                            e -> e == ((Item) unlockable).lockedWith,
                            "No puedes desbloquear " + unlockable + " con {}",
                            "nada de lo que llevas"
                    ).apply("Con que quieres desbloquearlo?", item -> {

                        // unlock
                        ((Item) unlockable).openable = Item.OPENABLE.CLOSED;
                        npc.getLocation().notifyNPCs(npc, npc + " desbloquea " + unlockable + ".");
                        return Result.done("Desbloqueas " + unlockable + ".");
                    });
                });
            }
            case LOCK -> {
                return command.main.require(
                        // you must see the element
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and it needs to be openable
                        e -> e instanceof Item && ((Item) e).openable != null && ((Item) e).lockedWith != null,
                        "No puedes bloquear {}.",
                        "nada"
                ).require(
                        // and be unlocked
                        e -> ((Item) e).openable != Item.OPENABLE.LOCKED,
                        "{} está ya bloqueado.",
                        "todo"
                ).apply("Que quieres bloquear?", lockable -> {

                    return command.secondary.require(
                            // we must have it
                            npc.elements::contains,
                            "No tienes {}.",
                            "nada con lo que bloquearlo"
                    ).require(
                            // and be the unlockable item
                            e -> e == ((Item) lockable).lockedWith,
                            "No puedes bloquear " + lockable + " con {}",
                            "nada de lo que llevas"
                    ).apply("Con que quieres bloquearlo?", item -> {

                        // lock
                        ((Item) lockable).openable = Item.OPENABLE.LOCKED;
                        npc.getLocation().notifyNPCs(npc, npc + " bloquea " + lockable + ".");
                        return Result.done("Bloqueas " + lockable + ".");
                    });
                });
            }
            case GO -> {
                if (command.direction == null) {
                    // no direction
                    // original game makes instead a 'go through'
                    return Result.moreNeeded("Hacia donde quieres ir?");
                }

                if (!(npc.getLocation() instanceof Location)) {
                    // inside an item
                    return Result.error("No puedes moverte mientras estás en " + npc.getLocation() + ".");
                }

                Utils.Pair<Location, Item> le = ((Location) npc.getLocation()).exits.get(command.direction);

                if (le == null) {
                    // no exit
                    return Result.error("No puedes ir hacia " + command.direction.description + ".");
                }

                Location newLocation = le.first;
                Item throughItem = le.second;

                if (throughItem != null && throughItem.openable == Item.OPENABLE.CLOSED) {
                    // closed exit
                    return Result.error(throughItem + " está cerrado/a."); // TODO: add a 'genre' to items to remove the ugly 'o/a'
                }
                if (throughItem != null && throughItem.openable == Item.OPENABLE.LOCKED) {
                    // locked exit
                    return Result.error(throughItem + " está bloqueado/a.");
                }

                // notify old npc
                npc.getLocation().notifyNPCs(npc, npc + " va hacia " + command.direction.description + ".");

                // move
                npc.moveTo(newLocation);

                // notify new npc
                npc.getLocation().notifyNPCs(npc, npc + " entra.");

                return Result.done("Te diriges hacia " + command.direction.description);
            }
            case FOLLOW -> {
                // check if we are inside something
                if (!(npc.getLocation() instanceof Location))
                    return Result.error("No puedes seguir a nadie desde aquí.");

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
                        otherNPC -> ((Location) npc.getLocation()).exits.entrySet().stream().anyMatch(l -> l.getValue().first.elements.contains(otherNPC)),
                        "No veo a {}.",
                        "nadie a quien seguir"
                ).apply("A quien quieres seguir?", toFollow -> {

                    // find direction and follow
                    for (Map.Entry<Word.Direction, Utils.Pair<Location, Item>> entry : ((Location) npc.getLocation()).exits.entrySet()) {
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
                        // it must be interactable
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and also we must have it
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
                        elementToGive.moveTo(whoToGiveItTo);
                        whoToGiveItTo.hear(npc + " te da " + elementToGive + ".");
                        return Result.done("Le das " + elementToGive + " a " + whoToGiveItTo + ".\n" + whoToGiveItTo + " te da las gracias.");
                    });
                });
            }
            case PICK -> {
                final Predicate<Element> insideOpenedContainer = e -> e.getLocation() instanceof Item && ((Item) e.getLocation()).openable == Item.OPENABLE.OPENED;
                return command.main.require(
                        // it must be interactable
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and also must be in the location of the npc, or inside another item
                        e -> npc.getLocation().elements.contains(e) || insideOpenedContainer.test(e),
                        "No puedes coger {} directamente.",
                        "nada"
                ).require(
                        // it must have less weight
                        e -> e.weight < npc.weight,
                        "{} pesa demasiado.",
                        "todo"
                ).apply("Que quieres coger?", pickable -> {

                    // pick
                    pickable.moveTo(npc);
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
                    dropable.moveTo(npc.getLocation());
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
                    npc.hear("Hablas con " + toSay);
                    ((NPC) toSay).ask(npc, command.sequence);
                    return Result.done(""); // the notification is above, otherwise the output order would be wrong
                });
            }
//            case HELP -> {
//                return Result.done(npc.game.world.requiredObjectives.get(0).first + ".");
//            }
//            case SCORE -> {
//                return Result.done(npc.game.getCompletion());
//            }
            case PUT -> {
                return command.main.require(
                        // we must have it
                        npc.elements::contains,
                        "No tienes {}.",
                        "nada para poner"
                ).apply("Que quieres poner?", elementToGive -> {

                    // check what to give it to
                    return command.secondary.require(
                            // it must be an item
                            e -> e instanceof Item,
                            "No puedes poner cosas en {}.",
                            "nada"
                    ).require(
                            // and be interactable
                            interactable::contains,
                            "No veo {}.",
                            "nada desde aquí"
                    ).require(
                            // and must be open
                            item -> ((Item) item).openable == Item.OPENABLE.OPENED,
                            "{} está cerrado.",
                            "todo"
                    ).apply("Donde lo quieres poner?", Word.Preposition.AT.alias + " ", container -> {

                        // put
                        elementToGive.moveTo(container);
                        return Result.done("Pones " + elementToGive + " en " + container + ".");
                    });
                });
            }
            case KILL -> {
                return command.main.require(
                        // the element must be an npc
                        e -> e instanceof NPC,
                        "No puedes atacar a {}.",
                        "nadie"
                ).require(
                        // and be interactable
                        interactable::contains,
                        "No veo a {} por aquí.",
                        "nadie"
                ).apply("A quien quieres atacar?", attack -> {

                    // attack
                    ((NPC) attack).lastAttackedBy = npc;
                    if (attack.weight > npc.weight) {
                        // stronger, the attack fails
                        attack.hear(npc + " te ataca pero no te provoca ningún daño.");
                        return Result.done("Atacas a " + attack + " pero el esfuerzo es en vano. Su defensa es muy fuerte.");
                    }
                    if (attack.weight == npc.weight) {
                        // same strength, the attack fails
                        attack.hear(npc + " te ataca pero solo logra hacerte daño.");
                        return Result.done("Atacas a " + attack + " pero no eres suficientemente fuerte y solo logras hacerle daño.");
                    }
                    // weaker, the attack success
                    attack.hear(npc + " te ataca. Con un golpe certero, te parte el cráneo.");
                    attack.alive = false;
                    attack.moveTo(null);
                    return Result.done("Atacas a " + attack + ". Con un golpe certero le partes el cráneo.");
                });
            }
            case EAT -> {
                return command.main.require(
                        // the element must be an item or npc
                        e -> e instanceof Item || e instanceof NPC,
                        "No puedes comer {}.",
                        "nadie"
                ).require(
                        // and be interactable
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and be lighter
                        e -> e.weight < npc.weight,
                        "{} pesa demasiado para comerlo.",
                        "todo"
                ).apply("Que quieres comer?", food -> {

                    // eat
                    food.alive = false;
                    food.moveTo(null);
                    food.hear(npc + " te come.");

                    return Result.done("Te comes " + food + ".");
                });
            }
        }


        return Result.error("Aún no se hacer eso!");
    }

}

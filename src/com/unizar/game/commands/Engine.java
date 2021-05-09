package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

        final Element location = npc.getLocation();
        if (location == null) return Result.error("Estás muerto");

        if (command.beforeCommand != null) {
            // multiple commands, run sub first
            final Result result = execute(npc, command.beforeCommand);
            // then this
            command.beforeCommand = null;
            // merge and return
            result.merge(execute(npc, command));
            return result;
        }

        final List<Element> interactable = location.getInteractable();
        interactable.remove(npc);

        switch (command.action) {
            case WAIT:
                return Result.done("Esperas. El tiempo pasa.");
//            case INVENTORY:
//                return Result.error("Tu inventario se muestra ahora a la derecha");
            case SAVE:
                npc.game.save();
                return Result.done("[también puedes usar F6 para guardar]");
            case LOAD:
                npc.game.load();
                return Result.done("[también puedes usar F9 para guardar]");
            case QUIT:
                npc.game.exit();
                return Result.done("[también puedes pulsar la X de la ventana]");
            case HELP:
                npc.game.help();
                return Result.done("[también puedes pulsar F1]"); // TODO: show a help string for the current location instead

            case OPEN:
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
                        "{} ya está abierto/a.",
                        "todo"
                ).require(
                        // and not be locked by an element
                        e -> ((Item) e).openable != Item.OPENABLE.LOCKED,
                        "{} está bloqueado/a.",
                        "todo"
                ).apply("Que quieres abrir?", element -> {
                    // open
                    ((Item) element).openable = Item.OPENABLE.OPENED;
                    location.notifyNPCs(npc, npc + " abre " + element + ".");
                    return Result.done("Abres " + element.getDescription());
                });
            case CLOSE:
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
                        "{} ya está cerrado/a.",
                        "todo"
                ).require(
                        // nor locked
                        e -> ((Item) e).openable != Item.OPENABLE.LOCKED,
                        "{} está bloqueado/a.",
                        "todo"
                ).apply("Que quieres cerrar?", element -> {

                    // close
                    ((Item) element).openable = Item.OPENABLE.CLOSED;
                    location.notifyNPCs(npc, npc + " cierra " + element + ".");
                    return Result.done("Cierras " + element + ".");
                });
            case UNLOCK:
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
                        "{} ya está desbloqueado.",
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
                        location.notifyNPCs(npc, npc + " desbloquea " + unlockable + ".");
                        return Result.done("Desbloqueas " + unlockable + ".");
                    });
                });
            case LOCK:
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
                        "{} ya está bloqueado.",
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
                        location.notifyNPCs(npc, npc + " bloquea " + lockable + ".");
                        return Result.done("Bloqueas " + lockable + ".");
                    });
                });
            case GO:
                if (command.direction == null) {
                    // no direction
                    // original game makes instead a 'go through'
                    return Result.moreNeeded("Hacia donde quieres ir?");
                }

                if (!(location instanceof Location)) {
                    // inside an item
                    return Result.error("No puedes moverte mientras estás en " + location + ".");
                }

                Utils.Pair<Location, Item> le = ((Location) location).exits.get(command.direction);
                if (le == null || !npc.lugares.contains(le.first)) { // TODO: arreglar
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
                location.notifyNPCs(npc, npc + " va hacia " + command.direction.description + ".");

                // move
                npc.moveTo(newLocation);

                // notify new npc
                newLocation.notifyNPCs(npc, npc + " entra.");

                return Result.done("Te diriges hacia " + command.direction.description);
            case FOLLOW:
                // check if we are inside something
                if (!(location instanceof Location))
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
                        otherNPC -> ((Location) location).exits.entrySet().stream().anyMatch(l -> l.getValue().first.elements.contains(otherNPC)),
                        "No veo a {}.",
                        "nadie a quien seguir"
                ).apply("A quien quieres seguir?", toFollow -> {

                    // find direction and follow
                    for (Map.Entry<Word.Direction, Utils.Pair<Location, Item>> entry : ((Location) location).exits.entrySet()) {
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
            case GIVE:
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
            case PICK:
                return command.main.require(
                        // it must be interactable
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and not be in yout inventory
                        e -> !npc.elements.contains(e),
                        "Ya tienes {}.",
                        "todo"
                ).require(
                        // and not be carried by another npc
                        e -> !(location instanceof NPC),
                        "No puedes coger {} de otro personaje.",
                        "nada"
                ).require(
                        // and not be inside a closed item
                        e -> !(e.getLocation() instanceof Item) || ((Item) e.getLocation()).openable == null || ((Item) e.getLocation()).openable == Item.OPENABLE.OPENED,
                        "No puedes coger {} de un recipiente cerrado.",
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
            case DROP:
                return command.main.require(
                        // You must have it
                        npc.elements::contains,
                        "No tienes {}.",
                        "nada"
                ).apply("Que quieres tirar?", dropable -> {

                    // drop
                    dropable.moveTo(location);
                    return Result.done("Tiras " + dropable);
                });
            case EXAMINE:
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
                    final String examination = ((Item) element).examine(npc);
                    npc.getLocation().notifyNPCs(npc, npc + " dice: " + examination);
                    return Result.done("Examinas " + element + ".\n" + examination);
                });

            case SAY:
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
            case SCORE:
                return Result.done(npc.game.getCompletion());
            case PUT:
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
            case KILL:
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
                    // TODO: comprobar
                    //npc ataca, attack defiende
                    int damage = 0;
                    int dice = (int) Math.floor(Math.random() * 20);

                    if (dice == 20) { //critico, mata
                        damage = 1000000;
                    } else if (dice == 0) { //pifia, no hace daño
                        damage = 0;
                    } else { //normal
                        damage = npc.fuerza + dice;
                    }

                    ((NPC) attack).vida = ((NPC) attack).vida - damage;

                    if (damage == 0) {
                        attack.hear(npc + " te ataca pero no te provoca ningún daño.");
                        return Result.done("Atacas a " + attack + " pero el esfuerzo es en vano. Su defensa es muy fuerte.");
                    }

                    if (((NPC) attack).vida <= 0) { //muere
                        attack.hear(npc + " te ataca. Con un golpe certero, te parte el cráneo.");
                        attack.moveTo(null);
                        return Result.done("Atacas a " + attack + ". Con un golpe certero le partes el cráneo.");
                    }
                    //ha sufrido daño pero no ha muerto
                    attack.hear(npc + " te ataca pero solo logra hacerte daño.");
                    return Result.done("Atacas a " + attack + " pero no eres suficientemente fuerte y solo logras hacerle daño.");
                });
            case EAT:
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
                    food.moveTo(null);
                    food.hear(npc + " te come.");

                    return Result.done("Te comes " + food + ".");
                });
            case DIG:
                return command.main.require(
                        // it must be interactable
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and allow digging
                        e -> e.hiddenElements.containsKey(Word.Action.DIG),
                        "No puedes cavar en {}.",
                        "nada"
                ).apply("Donde quieres cavar?", dig -> {

                    // dig
                    final Element found = dig.hiddenElements.remove(Word.Action.DIG);
                    found.moveTo(dig);
                    return Result.done("Cavas en " + dig + ". Descubres " + found + ".");
                });
            case BREAK:
                return command.main.require(
                        // it must be interactable
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and allow breaking
                        e -> e.hiddenElements.containsKey(Word.Action.BREAK),
                        "No puedes romper {}.",
                        "nada"
                ).apply("Que quieres romper?", breakItem -> {

                    // break

                    // move the unhidden element to the same parent
                    final Element found = breakItem.hiddenElements.remove(Word.Action.BREAK);
                    if (found != null) found.moveTo(breakItem.getLocation());
                    // replace also in location exits
                    breakItem.game.findElementsByClassName(Location.class).forEach(testLocation ->
                            testLocation.exits.forEach((__, testExit) -> {
                                if (testExit.second == breakItem) testExit.second = (Item) found;
                            })
                    );
                    // remove the broken element
                    breakItem.moveTo(null);
                    return Result.done("Rompes " + breakItem + "." + (found == null ? "" : " Descubres " + found + "."));
                });

            case LOOK:

                if (command.main.wasSpecified() && !command.secondary.wasSpecified()) {
                    // do a EXAMINE instead
                    command.action = Word.Action.EXAMINE;
                    return execute(npc, command);
                }

                // we must be in a location
                if (!(location instanceof Location)) {
                    return Result.error("No puedes mirar desde aquí.");
                }

                return command.secondary.require(
                        // it must be interactable
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // the element must be an item
                        e -> e instanceof Item,
                        "No puedes mirar hacia {}.",
                        "nada"
                ).require(
                        // it must be in an exit
                        item -> ((Location) location).exits.values().stream().map(li -> li.second).filter(Objects::nonNull).collect(Collectors.toSet()).contains(item),
                        "No hay nada hacia {}.",
                        "ningun sitio"
                ).apply("Hacia donde quieres mirar?", door -> {

                    // look
                    return Result.done("Miras hacia " + door + ". Puedes ver " +
                            ((Location) location).exits.values().stream().filter(li -> li.second == door).map(li -> li.first).findAny().orElse(null).getDescription()
                    );
                });

            case THROW:
                return command.main.require(
                        // it must be interactable
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and also we must have it
                        npc.elements::contains,
                        "No tienes {}.",
                        "nada que lanzar"
                ).apply("Que quieres lanzar?", elementToThrow -> {

                    // check who to give it to
                    return command.secondary.require(
                            // it must be interactable
                            interactable::contains,
                            "No veo {} desde aquí.",
                            "nada"
                    ).require(
                            // the element must be an item
                            e -> e instanceof Item,
                            "No puedes lanzar hacia {}.",
                            "nada"
                    ).require(
                            // it must be in an exit
                            item -> ((Location) location).exits.values().stream().map(li -> li.second).filter(Objects::nonNull).collect(Collectors.toSet()).contains(item),
                            "No hay nada hacia {}.",
                            "ningun sitio"
                    ).apply("Hacia donde lo quieres lanzar?", Word.Preposition.ACROSS.alias + " ", throwAcross -> {

                        Location whereToThrow = ((Location) location).exits.values().stream().filter(li -> li.second == throwAcross).map(li -> li.first).findAny().orElse(null);

                        // throw
                        elementToThrow.moveTo(whereToThrow);
                        whereToThrow.notifyNPCs(npc, npc + " lanza " + elementToThrow + ".");
                        return Result.done("Lanzas " + elementToThrow + " al otro lado de " + throwAcross + ".");
                    });
                });

            case PULL:
                return command.secondary.require(
                        // it must be interactable
                        interactable::contains,
                        "No veo {} por aquí.",
                        "nada"
                ).require(
                        // and allow pulling
                        e -> e.hiddenElements.containsKey(Word.Action.PULL),
                        "No puedes tirar de {}.",
                        "nada"
                ).apply("De que quieres tirar?", pull -> {

                    // pull
                    final Element found = pull.hiddenElements.get(Word.Action.PULL);
                    found.moveTo(pull);
                    return Result.done("Tiras de " + pull + ". " + found + " viene a este lado.");
                });

            case TIRAR:
                if (!command.main.wasSpecified() && command.secondary.wasSpecified()) {
                    // "TIRAR from something" means pull
                    command.action = Word.Action.PULL;
                } else {
                    // "TIRAR something" means DROP
                    command.action = Word.Action.DROP;
                }
                return execute(npc, command);

            case WEAR:
                return command.main.require(
                        // You must have it
                        npc.elements::contains,
                        "No tienes {}.",
                        "nada"
                ).apply("Que quieres ponerte?", wearable -> {

                    // wear
                    npc.elements.remove(wearable);
                    npc.wearables.add(wearable);
                    return Result.done("Te pones " + wearable);
                });
            case UNWEAR:
                return command.main.require(
                        // You must wear it
                        npc.wearables::contains,
                        "No llevas puesto {}.",
                        "nada"
                ).apply("Que quieres quitarte?", wearable -> {

                    // unwear
                    npc.wearables.remove(wearable);
                    npc.elements.add(wearable);
                    return Result.done("Te quitas " + wearable);
                });
        }

        return Result.error("Aún no se hacer eso!");
    }

}

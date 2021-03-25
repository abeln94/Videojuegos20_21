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

        final List<Element> interactable = npc.location.getInteractable();

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

                command.reFilterMainElement(List.of(
                        interactable::contains, // first filter by interactable
                        e -> e instanceof Item && ((Item) e).opened == Boolean.FALSE // then filter by openable
                ));

                if (command.mainElement.isEmpty()) {
                    if (command.mainElementDescription != null)
                        return Result.error("No veo '" + command.mainElementDescription + "'");
                    else
                        return Result.error("No veo nada que pueda abrir");
                }
                if (command.mainElement.size() >= 2) {
                    return Result.moreNeeded("Que quieres que abra?");
                }

                Element element = command.mainElement.iterator().next();

                if (!(element instanceof Item)) {
                    return Result.error("No puedo abrir " + element);
                } else if (((Item) element).opened) {
                    return Result.error(element + " ya está abierto/a");
                } else {
                    ((Item) element).opened = true;
                    return Result.done("Abres " + element);
                }
            }
            case CLOSE -> {

                command.reFilterMainElement(List.of(
                        interactable::contains, // first filter by interactable
                        e -> e instanceof Item && ((Item) e).opened == Boolean.TRUE // then filter by closeable
                ));

                if (command.mainElement.isEmpty()) {
                    if (command.mainElementDescription != null)
                        return Result.error("No veo '" + command.mainElementDescription + "'");
                    else
                        return Result.error("No veo nada que pueda cerrar");
                }
                if (command.mainElement.size() > 2) {
                    return Result.moreNeeded("Que quieres que cierre?");
                }

                Element element = command.mainElement.iterator().next();

                if (!(element instanceof Item)) {
                    return Result.error("No puedo cerrar " + element);
                } else if (!((Item) element).opened) {
                    return Result.error(element + " ya está cerrado/a");
                } else {
                    ((Item) element).opened = false;
                    return Result.done("Cierras " + element);
                }
            }
            case GO -> {
                if (command.direction == null) {
                    // no direction
                    // original game makes instead a 'go through'
                    return Result.moreNeeded("Hacia donde quieres que vaya?");
                }

                if (!(npc.location instanceof Location)) {
                    // inside an item
                    return Result.error("No puedes moverte mientras estás en " + npc.location);
                }

                Utils.Pair<Location, Item> le = ((Location) npc.location).exits.get(command.direction);

                if (le == null) {
                    // no exit
                    return Result.error("No puedes ir hacia " + command.direction.name);
                }

                Location newLocation = le.first;
                Item throughItem = le.second;

                if (throughItem != null && !throughItem.opened) {
                    // closed exit
                    return Result.error(throughItem + " está cerrado/a");
                }

                // notify old npc
                npc.location.say(npc, npc + " va hacia " + command.direction.name);

                // move
                npc.location.elements.remove(npc);
                npc.location = newLocation;
                npc.location.elements.add(npc);

                // notify new npc
                npc.location.say(npc, npc + " entra");

                return Result.done("Te diriges hacia " + command.direction.name);
            }
            case FOLLOW -> {
                Predicate<Element> followable = otherNPC -> ((Location) npc.location).exits.entrySet().stream().anyMatch(l -> l.getValue().first.elements.contains(otherNPC));

                command.reFilterMainElement(List.of(
                        e -> e instanceof NPC,
                        followable
                ));

                if (command.mainElement == null) return Result.moreNeeded("A quien quieres seguir?");

                if (!(command.mainElement instanceof NPC))
                    return Result.error("No puedo seguir a " + command.mainElement);

                if (!(npc.location instanceof Location)) return Result.error("No puedes seguir a nadie desde aquí");

                if (interactable.contains(command.mainElement))
                    return Result.error("Ya estás con " + command.mainElement);

                for (Map.Entry<Word.Direction, Utils.Pair<Location, Item>> entry : ((Location) npc.location).exits.entrySet()) {
                    if (entry.getValue().first.elements.contains(command.mainElement)) {
                        Result result = execute(npc, Command.go(entry.getKey()));
                        if (result.done) {
                            return Result.done("Sigues a " + command.mainElement);
                        } else {
                            return Result.error("No puedes seguir a " + command.mainElement + " desde aquí");
                        }
                    }
                }
                return Result.error("No puedes seguir a " + command.mainElement + " desde aquí");
            }
        }


        return Result.error("Aún no se hacer eso!");
    }
}

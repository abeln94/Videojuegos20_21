package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;

import java.util.Map;

public class Engine {

    public Result applyCommand(NPC npc, Command command) {
        System.out.println(npc + " " + command);

        if (command.action == null) {
            return Result.error("Como dices?");
        }


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
                if (command.element == null) return Result.moreNeeded("Que quieres que abra?");

                if (!(command.element instanceof Item) || ((Item) command.element).opened == null) {
                    return Result.error("No puedo abrir " + command.element);
                } else if (((Item) command.element).opened) {
                    return Result.error(command.element + " ya está abierto/a");
                } else {
                    ((Item) command.element).opened = true;
                    return Result.done("Abres " + command.element);
                }
            }
            case CLOSE -> {
                if (command.element == null) return Result.moreNeeded("Que quieres que cierre?");

                if (!(command.element instanceof Item) || ((Item) command.element).opened == null) {
                    return Result.error("No puedo cerrar " + command.element);
                } else if (!((Item) command.element).opened) {
                    return Result.error(command.element + " ya está cerrado/a");
                } else {
                    ((Item) command.element).opened = false;
                    return Result.done("Cierras " + command.element);
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
                    return Result.error("No puedes ir hacia el " + command.direction.name);
                }

                Location newLocation = le.first;
                Item throughItem = le.second;

                if (throughItem != null && !throughItem.opened) {
                    // closed exit
                    return Result.error(throughItem + " está cerrado/a");
                }

                // notify old npc
                npc.location.say(npc, npc + " va hacia el " + command.direction.name);

                // move
                npc.location.elements.remove(npc);
                npc.location = newLocation;
                npc.location.elements.add(npc);

                // notify new npc
                npc.location.say(npc, npc + " entra");

                return Result.done("Te diriges al " + command.direction.name);
            }
            case FOLLOW -> {
                if (command.element == null) return Result.moreNeeded("A quien quieres seguir?");

                if (!(command.element instanceof NPC)) return Result.error("No puedo seguir a " + command.element);

                if (!(npc.location instanceof Location)) return Result.error("No puedes seguir a nadie desde aquí");

                for (Map.Entry<Word.Direction, Utils.Pair<Location, Item>> entry : ((Location) npc.location).exits.entrySet()) {
                    if (entry.getValue().first.elements.contains(command.element)) {
                        Result result = applyCommand(npc, Command.go(entry.getKey()));
                        if (result.done) {
                            return Result.done("Sigues a " + command.element);
                        } else {
                            return Result.error("No puedes seguir a " + command.element + " desde aquí");
                        }
                    }
                }
                return Result.error("No puedes seguir a " + command.element + " desde aquí");
            }
        }


        return Result.error("Aún no se hacer eso!");
    }
}

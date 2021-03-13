package com.unizar.game;

import com.unizar.Utils;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;

public class Engine {
    public static class Result {
        public boolean done = false;
        public boolean requiresMore = false;
        public String output = null;

        static Result done(String output) {
            Result result = new Result();
            result.done = true;
            result.output = output;
            return result;
        }

        static Result moreNeeded(String output) {
            Result result = new Result();
            result.requiresMore = true;
            result.output = output;
            return result;
        }

        static Result error(String output) {
            Result result = new Result();
            result.output = output;
            return result;
        }
    }

    public Result applyCommand(NPC npc, Word.Adverbs adverb, Word.Action action, Word.Preposition preposition, Word.Direction direction, Element element) {
        System.out.println(npc + " - " + adverb + "  - " + action + " - " + preposition + " - " + direction + " - " + element);

        if (action == null) {
            return Result.error("Como dices?");
        }


        switch (action) {
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
            case SAY -> {

            }
            case OPEN -> {
                if (element == null) return Result.moreNeeded("Que quieres que abra?");

                if (!(element instanceof Item) || ((Item) element).opened == null) {
                    return Result.error("No puedo abrir " + element);
                } else if (((Item) element).opened) {
                    return Result.error(element + " ya está abierto/a");
                } else {
                    ((Item) element).opened = true;
                    return Result.done("Abres " + element);
                }
            }
            case CLOSE -> {
                if (element == null) return Result.moreNeeded("Que quieres que cierre?");

                if (!(element instanceof Item) || ((Item) element).opened == null) {
                    return Result.error("No puedo cerrar " + element);
                } else if (!((Item) element).opened) {
                    return Result.error(element + " ya está cerrado/a");
                } else {
                    ((Item) element).opened = false;
                    return Result.done("Cierras " + element);
                }
            }
            case GO -> {
                if (direction == null) {
                    // no direction
                    // original game makes instead a 'go through'
                    return Result.moreNeeded("Hacia donde quieres que vaya?");
                }

                if (!(npc.location instanceof Location)) {
                    // inside an item
                    return Result.error("No puedes moverte mientras estás en " + npc.location);
                }

                Utils.Pair<Location, Item> le = ((Location) npc.location).exits.get(direction);

                if (le == null) {
                    // no exit
                    return Result.error("No puedes ir hacia el " + direction.name);
                }

                Location newLocation = le.first;
                Item throughItem = le.second;

                if (throughItem != null && !throughItem.opened) {
                    // closed exit
                    return Result.error(throughItem + " está cerrado/a");
                }

                // notify old npc
                npc.location.say(npc, npc + " va hacia el " + direction.name + ".");

                // move
                npc.location.elements.remove(npc);
                npc.location = newLocation;
                npc.location.elements.add(npc);

                // notify new npc
                npc.location.say(npc, npc + " entra.");

                return Result.done("Te diriges al " + direction.name);
            }
        }


        return Result.error("Aún no se hacer eso!");
    }
}

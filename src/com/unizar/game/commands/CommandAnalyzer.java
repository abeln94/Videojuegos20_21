package com.unizar.game.commands;

import com.unizar.game.Game;
import com.unizar.game.Window;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Holdable;
import com.unizar.game.elements.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CommandAnalyzer implements Window.InputListener {
    private final Game game;

    public CommandAnalyzer(Game game) {
        this.game = game;
    }


    private String appendableCommand = "";

    @Override
    public void onText(String rawText) {
        // skip empty lines
        if (rawText.isEmpty()) return;

        // write command
        game.addOutput("> " + rawText);
        String text = appendableCommand + " " + rawText.toLowerCase(Locale.ROOT) + " ";
        appendableCommand = "";

        // find actions
        List<Command> commands = Arrays.stream(Command.values()).filter(c -> text.contains(" " + c.name + " ")).collect(Collectors.toList());

        if (commands.isEmpty()) {
            // no command
            game.addOutput("No se hacer " + text);
            return;
        }

        boolean acted = false;

        Player player = game.data.getPlayer();

        for (Command command : commands) {

            // find all actors where the player can do that command
            List<Element> actors = game.data.getInteractables(player).stream()
                    .filter(h -> h.doCommand(command, player) != null)
                    .collect(Collectors.toList());

            // find other actors explicitly named by the player
            List<Holdable> otherActors = game.data.getElements(Holdable.class).stream()
                    .filter(a -> !actors.contains(a))
                    .filter(a -> text.contains(" " + a.name + " "))
                    .collect(Collectors.toList());

            if (otherActors.isEmpty()) {
                // not expecting another actor
                if (actors.isEmpty()) {
                    // nothing to do that
                    game.addOutput("No tengo nada a lo que hacer eso");
                } else if (actors.size() == 1) {
                    // only one actor, and no others, act
                    game.addOutput(actors.get(0).doCommand(command, player).get());
                    acted = true;
                } else if (text.matches(" tod[oa]s? ")) {
                    // specifically act in all of them
                    for (Element actor : actors) {
                        game.addOutput(actor.doCommand(command, player).get());
                    }
                    acted = true;
                } else if (commands.size() == 1) {
                    // multiple actors, and this is the only command, ask to which one
                    appendableCommand = text;
                    game.addOutput("El que/a quién?");
                } else {
                    // multiple actors, and multiple commands, just exit
                    game.addOutput("Tienes que decirme el que o a quién.");
                }
            } else {
                // perhaps the user is expecting other elements
                game.addOutput("No puedo hacer eso ahora");
            }

        }

        if (acted) {
            // elements actions
            game.data.nonPlayerAct();
        }

    }

    private String matchAndRemove(String command, String match) {
        if (command.matches(match)) {
            return command.replaceAll(match, "").strip();
        } else {
            return null;
        }
    }


}

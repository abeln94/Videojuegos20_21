package com.unizar.game.commands;

import com.unizar.game.Game;
import com.unizar.game.Window;

import java.util.Locale;

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

    }

    private String matchAndRemove(String command, String match) {
        if (command.matches(match)) {
            return command.replaceAll(match, "").strip();
        } else {
            return null;
        }
    }


}

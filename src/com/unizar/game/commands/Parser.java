package com.unizar.game.commands;

import com.unizar.game.Game;
import com.unizar.game.Window;
import com.unizar.game.elements.Location;

import java.util.List;

/**
 * Basic parser, converts a string into a command simply by searching for the words
 */
public class Parser implements Window.InputListener {

    // ------------------------- -------------------------

    private final Game game;

    public Parser(Game game) {
        this.game = game;
    }

    /**
     * To allow 'ask more info'
     */
    private String appendableCommand = "";


    /**
     * When the user enters a text
     *
     * @param rawText user's text
     */
    @Override
    public void onText(String rawText) {
        // skip empty lines
        if (rawText.isEmpty()) return;

        // write command
        game.addOutput("> " + rawText);
        Word.Token[] elementTokens = game.world.elements.stream()
                .filter(e -> !(e instanceof Location))
                .flatMap(e -> Word.separateWords(e.name).stream())
                .distinct()
                .map(Word.ElementToken::new)
                .toArray(Word.Token[]::new); // TODO: move as constant after game initialization
        Command command = new Command(appendableCommand + rawText, elementTokens, game.world.elements);
        appendableCommand = "";

        // execute
        Result result = game.engine.execute(game.getPlayer(), command);

        // add output
        game.addOutput(result.output);

        // check if requires more
        if (result.requiresMore) {
            appendableCommand = rawText + " ";
        }

        // if was ok, let other elements act
        if (result.done) game.afterPlayer();
    }

    /**
     * Returns the first element, or null if empty
     */
    static public <T> T firstOrNull(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }

}

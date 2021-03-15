package com.unizar.game.commands;

import com.unizar.game.Game;
import com.unizar.game.Window;
import com.unizar.game.elements.Element;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic parser, converts a string into a command simply by searching for the words
 */
public class Parser implements Window.InputListener {
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
        List<String> words = Arrays.asList((appendableCommand + " " + rawText).toLowerCase().split(" +"));
        appendableCommand = "";

        // get matching predefined words
        Word.Matches matches = Word.getWords(words);

        // get matching interactable elements
        List<Element> interactable = game.getPlayer().location.getInteractable();
        interactable.addAll(game.world.getGlobalElements());
        Element element = firstOrNull(
                interactable.stream()
                        .filter(e -> words.stream().anyMatch(Arrays.asList(e.name.toLowerCase().split(" +"))::contains)).collect(Collectors.toList())
        );

        // execute
        Result result = game.engine.execute(game.getPlayer(), new Command(firstOrNull(matches.adverbs), firstOrNull(matches.actions), firstOrNull(matches.prepositions), firstOrNull(matches.directions), element));

        // add output
        game.addOutput(result.output);

        // check if requires more
        if (result.requiresMore) {
            appendableCommand = rawText;
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

package com.unizar.game.commands;

import com.unizar.game.Game;
import com.unizar.game.Window;
import com.unizar.game.elements.Element;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Parser implements Window.InputListener {
    private final Game game;

    public Parser(Game game) {
        this.game = game;
    }

    private String appendableCommand = "";


    @Override
    public void onText(String rawText) {
        // skip empty lines
        if (rawText.isEmpty()) return;

        // write command
        game.addOutput("> " + rawText);
        List<String> words = Arrays.asList((appendableCommand + " " + rawText).toLowerCase().split(" +"));
        appendableCommand = "";

        Word.Matches matches = Word.getWords(words);
        Element element = firstOrNull(
                game.getPlayer().location.getInteractable().stream()
                        .filter(e -> words.stream().anyMatch(Arrays.asList(e.name.toLowerCase().split(" +"))::contains)).collect(Collectors.toList())
        );

        Result result = game.engine.applyCommand(game.getPlayer(), new Command(firstOrNull(matches.adverbs), firstOrNull(matches.actions), firstOrNull(matches.prepositions), firstOrNull(matches.directions), element));

        game.addOutput(result.output);

        if (result.requiresMore) {
            appendableCommand = rawText;
        }

        if (result.done) game.afterPlayer();
    }

    static public <T> T firstOrNull(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }

}

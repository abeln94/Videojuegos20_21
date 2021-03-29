package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.Game;
import com.unizar.game.Window;
import com.unizar.game.elements.Element;

import java.util.List;
import java.util.Set;

/**
 * Basic parser, converts a string into a command simply by searching for the words
 */
public class Parser implements Window.InputListener {

    // ------------------------- game -------------------------

    private final Game game;

    public Parser(Game game) {
        this.game = game;
    }

    // ------------------------- data -------------------------

    /**
     * To allow 'ask more info'
     */
    private String appendableCommand = "";

    // ------------------------- parser -------------------------


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
        Command command = parse(appendableCommand + rawText, game.world.elements);
        appendableCommand = "";

        // execute
        Result result = game.engine.execute(game.getPlayer(), command);

        // spanish is difficult
        result.output = result.output.replaceAll(" a el ", " al ");

        // add output
        game.addOutput(result.output);

        // check if requires more
        if (result.requiresMore != null) {
            appendableCommand = rawText + " " + result.requiresMore + " ";
        }

        // if was ok, let other elements act
        if (result.done) game.afterPlayer();
    }

    /**
     * Parse the sentence
     *
     * @param sentence user input
     * @param elements game elements
     */
    public Command parse(String sentence, Set<Element> elements) {
        Command command = new Command(null, null, null, new Command.FilterableElements(elements), new Command.FilterableElements(elements));

        List<String> words = Word.separateWords(sentence);

        boolean isSecondElement = false;
        for (String word : words) {
            if (word.isEmpty()) continue;
            Utils.Pair<Word.Type, Word.Token> parsing = Word.parse(word, elements);
            switch (parsing.first) {
                case ACTION -> {
                    if (command.action != null) {
                        command.parseError = true;
                        return command;
                    }
                    command.action = (Word.Action) parsing.second;
                }
                case DIRECTION -> {
                    if (command.direction != null) {
                        command.parseError = true;
                        return command;
                    }
                    command.direction = (Word.Direction) parsing.second;
                }
                case MODIFIER -> {
                    if (command.modifier != null) {
                        command.parseError = true;
                        return command;
                    }
                    command.modifier = (Word.Modifier) parsing.second;
                }
                case PREPOSITION -> {
                    // next element will be the second element
                    isSecondElement = true;
                }
                case ELEMENT -> {
                    if (isSecondElement) command.secondary.addDescriptionWord(word);
                    else command.main.addDescriptionWord(word);
                }
                case MULTIPLE -> {
                    command.parseError = true;
                    return command;
                }
                case UNKNOWN -> {
                    command.invalidToken = word;
                    command.parseError = true;
                    return command;
                }

                case IGNORE -> { // ignore
                }
            }
        }

        // special shortcuts
        if (command.action == null && command.direction != null) {
            // a direction without action is a go
            command.action = Word.Action.GO;
        }

        return command;
    }

}

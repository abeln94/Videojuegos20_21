package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.Game;
import com.unizar.game.Window;
import com.unizar.game.elements.Element;

import java.util.ArrayList;
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

    // ------------------------- input history -------------------------

    /**
     * Last entered inputs (ignores 'needsMore')
     */
    private final List<String> historyInput = new ArrayList<>();
    private int historyIndex = 0; // the top of the list is a special 'empty' input

    /**
     * Restores a previous entered input
     *
     * @param previous if true, will go back to the previous input, if false to a newer one
     */
    public void restoreInput(boolean previous) {
        if (previous) {
            if (historyIndex - 1 < 0) return;

            // go back in history
            historyIndex--;
        } else {
            if (historyIndex + 1 > historyInput.size()) return;

            // go forward in history
            historyIndex++;
        }
        // set
        game.window.setCommand(historyIndex == historyInput.size() ? "" : historyInput.get(historyIndex));
    }

    /**
     *
     */
    public void clearHistory() {
        historyInput.clear();
        historyIndex = 0;
    }

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
        Command command = parse(rawText);

        // execute
        Result result = game.engine.execute(game.getPlayer(), command);

        // spanish is difficult
        result.output = result.output.replaceAll("\\ba el\\b", "al");
        result.output = result.output.replaceAll("\\bde el\\b", "del");

        // add output
        game.addOutput(result.output);

        // check if requires more
        if (result.requiresMore != null) {
            game.window.setCommand(rawText + " " + result.requiresMore);
        } else {
            // add to history (if different)
            if (historyInput.isEmpty() || !rawText.equals(historyInput.get(historyInput.size() - 1))) {
                historyInput.add(rawText);
            }
            historyIndex = historyInput.size();
        }

        // if was ok, let other elements act
        if (result.done) game.afterPlayer();
    }

    /**
     * Parse the sentence
     *
     * @param sentence user input
     */
    public Command parse(String sentence) {
        Set<Element> elements = game.world.elements;
        Command command = new Command(null, null, null, null, new Command.FilterableElements(elements), new Command.FilterableElements(elements));

        // extract sequence
        sentence = sentence.replaceAll("\"\"", "\"");
        long quotations = sentence.chars().filter(c -> c == '"').count();
        if (quotations == 1) {
            // one is missing, just add it (this helps with the 'say what?' extension, which adds a single '"' before asking again)
            sentence = sentence + '"';
            quotations++;
        }
        if (quotations == 2) {
            // extract subsequence
            command.sequence = sentence.substring(sentence.indexOf('"') + 1, sentence.lastIndexOf('"'));
            sentence = sentence.replaceAll("\".*\"", " ");
        } else if (quotations != 0) {
            // invalid number of quotation marks
            command.parseError = true;
            return command;
        }

        // remove non-alphabetical chars
        sentence = sentence.replaceAll("[^0-9a-zA-ZñÑáéíóú]", " ");

        // when you say 'darme el mapa' the 'me' part is replaced by the player
        sentence = sentence.replaceAll("\\bdarme\\b", "a " + game.getPlayer().name + " dar");

        // get the words
        List<String> words = Word.separateWords(sentence);
        boolean isSecondElement = false;
        for (String word : words) {
            if (word.isEmpty()) continue;
            Utils.Pair<Word.Type, Object> parsing = Word.parse(word, elements);
            switch (parsing.first) {
                case ACTION -> {
                    if (command.action != null) {
                        command.parseError = true;
                        return command;
                    }
                    command.action = (Word.Action) parsing.second;
                    isSecondElement = false;
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

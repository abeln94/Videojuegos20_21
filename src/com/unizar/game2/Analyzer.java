package com.unizar.game2;

import com.unizar.Utils;
import com.unizar.game.commands.EngineException;

import java.util.List;
import java.util.Set;

public class Analyzer {
    public static Command2 analyze(String sentence, Set<String> elements, Set<String> actions, Set<String> modifiers) throws EngineException {
        Command2 command = new Command2();

        // remove non-alphabetical chars
        sentence = sentence.replaceAll("[^0-9a-zA-ZñÑáéíóú]", " ");

        // get the words
        List<String> words = Tokenizer.separateWords(sentence);

        String currentParameter = "";

        for (String word : words) {
            if (word.isEmpty()) continue;
            Utils.Pair<Tokenizer.Type, String> parsing = Tokenizer.parse(word, elements, actions, modifiers);
            switch (parsing.first) {
                case ACTION:
                    command.setAction(parsing.second);
                    break;
                case PARAMETER:
                    currentParameter = parsing.second;
                    command.initParameter(currentParameter);
                    break;
                case ELEMENT:
                    command.getParameter(currentParameter)
                            .addWord(parsing.second);
                    break;
                case ALL:
                    command.getParameter(currentParameter)
                            .markAsAll();
                    break;

                case ANY:
                    command.getParameter(currentParameter)
                            .markAsAny();
                    break;

                case UNKNOWN:
                    throw new EngineException("No entiendo '" + word + '"');

                case IGNORE: // ignore
                    break;
            }
        }

        return command;
    }
}

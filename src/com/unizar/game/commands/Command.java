package com.unizar.game.commands;

import com.unizar.Utils;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.NPC;

import java.util.List;
import java.util.Set;

/**
 * A processed command, contains the command elements for the engine
 */
public class Command {

    public NPC npc; // who executes this command

    public Word.Modifier modifier; // the modifier
    public Word.Action action; // the action

    public Word.Direction direction; // a direction
    public String sequence; // a sequence

    public FilterableElements main; // the main object
    public FilterableElements secondary; // the secondary object

    public Command beforeCommand = null; // something to run before this one

    // ------------------------- constructors -------------------------

    /**
     * Internal constructor
     */
    private Command(Word.Modifier modifier, Word.Action action, Word.Direction direction, String sequence, FilterableElements main, FilterableElements secondary) {
        this.modifier = modifier;
        this.action = action;
        this.direction = direction;
        this.sequence = sequence;
        this.main = main;
        this.secondary = secondary;
    }

    /**
     * Create a new empty command
     *
     * @param elements base list of elements for both main and secondary
     */
    public Command(Set<Element> elements) {
        this(null, null, null, null, new FilterableElements(elements), new FilterableElements(elements));
    }

    /**
     * A simple action without parameters
     */
    public static Command simple(Word.Action action) {
        return new Command(null, action, null, null, null, null);
    }

    /**
     * An action that requires an element
     */
    public static Command act(Word.Action action, Element element) {
        return new Command(null, action, null, null, new FilterableElements(element), null);
    }

    /**
     * An action that requires two elements
     */
    public static Command act(Word.Action action, Element mainElement, Element secondaryElement) {
        return new Command(null, action, null, null, new FilterableElements(mainElement), new FilterableElements(secondaryElement));
    }

    /**
     * A go action
     */
    public static Command go(Word.Direction direction) {
        return new Command(null, Word.Action.GO, direction, null, null, null);
    }

    public Command asNPC(NPC npc) {
        this.npc = npc; // TODO: set as a factory or something
        if (beforeCommand != null) beforeCommand.asNPC(npc);
        return this;
    }

    // ------------------------- generation -------------------------

    /**
     * Parse the sentence
     *
     * @param sentence user input
     * @param elements all the elements in the game
     */
    public static Command parse(String sentence, Set<Element> elements) throws EngineException {
        Command command = new Command(elements);

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
            throw new EngineException("Número inválido de comillas");
        }

        // remove non-alphabetical chars
        sentence = sentence.replaceAll("[^0-9a-zA-ZñÑáéíóú]", " ");

//        // when you say 'darme el mapa' the 'me' part is replaced by the player // TODO: move to when the command is executed
//        sentence = sentence.replaceAll("\\b([^ ]*)me\\b", "a " + game.getPlayer().name + " $1");

        // get the words
        List<String> words = Word.separateWords(sentence);
        boolean isSecondElement = false;
        for (String word : words) {
            if (word.isEmpty()) continue;
            Utils.Pair<Word.Type, Object> parsing = Word.parse(word, elements);
            switch (parsing.first) {
                case ACTION:
                    if (command.action != null) throw new EngineException("Solo puedes especificar una acción");

                    command.action = (Word.Action) parsing.second;
                    isSecondElement = false;
                    break;
                case DIRECTION:
                    if (command.direction != null) throw new EngineException("Solo puedes especificar una dirección");
                    command.direction = (Word.Direction) parsing.second;
                    break;
                case MODIFIER:
                    if (command.modifier != null) throw new EngineException("Solo puedes especificar un modificador");
                    command.modifier = (Word.Modifier) parsing.second;
                    break;
                case PREPOSITION:
                    // next element will be the second element
                    isSecondElement = true;
                    break;
                case ELEMENT:
                    if (isSecondElement) command.secondary.addDescriptionWord(word);
                    else command.main.addDescriptionWord(word);
                    break;

                case ALL:
                    if (isSecondElement) command.secondary.markAsAll();
                    else command.main.markAsAll();
                    break;

                case AND:
                    Command newCommand = new Command(elements).asNPC(command.npc);
                    newCommand.beforeCommand = command;
                    command = newCommand;
                    break;

                case UNKNOWN:
                    throw new EngineException("No entiendo '" + word + '"');

                case IGNORE: // ignore
                    break;
            }
        }

        // validate
        command.validate();

        return command;
    }

    /**
     * Prepares this command after parsing
     */
    private void validate() throws EngineException {

        // chech merge
        if (beforeCommand != null) {
            // validate first the subcommand if necessary
            beforeCommand.validate();

            // merge with the subcommand values
            if (modifier == null) modifier = beforeCommand.modifier;
            if (action == null) action = beforeCommand.action;
        }

        // special shortcuts
        if (action == null && direction != null) {
            // a direction without action is a go
            action = Word.Action.GO;
        }

        // check no action
        if (action == null) throw new EngineException("Que quieres que haga?");

    }

    // ------------------------- filters -------------------------

    @Override
    public String toString() {
        return npc + ": " + (modifier == null ? "" : modifier + " - ")
                + action
                + (direction == null ? "" : " - " + direction)
                + (main == null ? "" : " - " + main)
                + (secondary == null ? "" : " - " + secondary);
    }
}

package com.unizar.game.commands;

/**
 * The result of an execute command
 */
public class Result {

    /**
     * Whether the command was executed correctly
     */
    public boolean done = true;

    /**
     * Whether the command is incomplete and requires more info
     */
    public String requiresMore = null;

    /**
     * The message to show to the user regarding the command execution
     */
    public String output = null;

    // TODO: add a nextCommand field that will need to be run on next turn. Save here when multiple commands are executed

    // ------------------------- constructors -------------------------

    /**
     * Constructor: An ok result
     */
    public static Result done(String output) {
        Result result = new Result();
        result.output = output;
        return result;
    }

    /**
     * Constructor: A 'needs more input' result
     */
    public static Result moreNeeded(String output) {
        return moreNeeded(output, "");
    }

    /**
     * Constructor: A 'needs more input' result, with an appendable input string
     */
    public static Result moreNeeded(String output, String appendableInput) {
        Result result = new Result();
        result.done = false;
        result.requiresMore = appendableInput == null ? "" : appendableInput;
        result.output = output;
        return result;
    }

    /**
     * Constructor: An error result
     */
    public static Result error(String output) {
        Result result = new Result();
        result.done = false;
        result.output = output;
        return result;
    }

    // ------------------------- functions -------------------------

    public void merge(Result result) {
        assert done;
        done = result.done;
        requiresMore = result.requiresMore;
        output = (output == null ? "" : output + "\n") + result.output;
    }

    @Override
    public String toString() {
        return "[" + (done ? "OK" : requiresMore != null ? "WHAT?" : "ERROR") + "] " + output;
    }
}

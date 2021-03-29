package com.unizar.game.commands;

/**
 * The result of an execute command
 */
public class Result {

    /**
     * Whether the command was executed correctly
     */
    public boolean done = false;

    /**
     * Whether the command is incomplete and requires more info
     */
    public String requiresMore = null;

    /**
     * The message to show to the user regarding the command execution
     */
    public String output = null;

    /**
     * Constructor: An ok result
     */
    public static Result done(String output) {
        Result result = new Result();
        result.done = true;
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
        result.requiresMore = appendableInput == null ? "" : appendableInput;
        result.output = output;
        return result;
    }

    /**
     * Constructor: An error result
     */
    public static Result error(String output) {
        Result result = new Result();
        result.output = output;
        return result;
    }

    @Override
    public String toString() {
        return "[" + (done ? "OK" : requiresMore != null ? "WHAT?" : "ERROR") + "] " + output;
    }
}

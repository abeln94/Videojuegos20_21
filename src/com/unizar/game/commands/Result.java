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
    public boolean requiresMore = false;

    /**
     * The message to show to the user regarding the command execution
     */
    public String output = null;

    public static Result done(String output) {
        Result result = new Result();
        result.done = true;
        result.output = output;
        return result;
    }

    public static Result moreNeeded(String output) {
        Result result = new Result();
        result.requiresMore = true;
        result.output = output;
        return result;
    }

    public static Result error(String output) {
        Result result = new Result();
        result.output = output;
        return result;
    }

    @Override
    public String toString() {
        return "[" + (done ? "OK" : requiresMore ? "WHAT?" : "ERROR") + "] " + output;
    }
}

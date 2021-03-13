package com.unizar.game.commands;

public class Result {
    public boolean done = false;
    public boolean requiresMore = false;
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

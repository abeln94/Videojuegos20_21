package com.unizar.game.commands;

public class EngineException extends Exception {

    public String userError;

    public String newUserInput;

    public EngineException() {
    }

    public EngineException(String userError) {
        this.userError = userError;
    }

    public EngineException(String userError, String newUserInput) {
        this.userError = userError;
        this.newUserInput = newUserInput;
    }
}

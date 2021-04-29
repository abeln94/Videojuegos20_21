package com.unizar.game.commands;

import com.unizar.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps a log of the input commands
 */
public class History {

    // ------------------------- game -------------------------

    private final Game game;

    public History(Game game) {
        this.game = game;
    }

    // ------------------------- input history -------------------------

    /**
     * Last entered inputs
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
     * Clears the history
     */
    public void clearHistory() {
        historyInput.clear();
        historyIndex = 0;
    }

    /**
     * Adds an command to the history
     *
     * @param command to add
     */
    public void add(String command) {
        // add to history (if different)
        if (historyInput.isEmpty() || !command.equals(historyInput.get(historyInput.size() - 1))) {
            historyInput.add(command);
        }
        historyIndex = historyInput.size();
    }
}

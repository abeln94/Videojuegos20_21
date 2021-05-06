package com.unizar.game.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps a log of the input commands
 */
public class History {

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
    public String getPreviousInput(boolean previous) {
        if (previous) {
            if (historyIndex - 1 < 0) return null;

            // go back in history
            historyIndex--;
        } else {
            if (historyIndex + 1 > historyInput.size()) return null;

            // go forward in history
            historyIndex++;
        }

        // return
        return historyIndex == historyInput.size() ? "" : historyInput.get(historyIndex);
    }

    /**
     * Clears the saved history
     */
    public void clearHistory() {
        historyInput.clear();
        historyIndex = 0;
    }

    /**
     * Adds a string into the history
     *
     * @param rawText text to add
     */
    public void add(String rawText) {
        // add to history (if different)
        if (historyInput.isEmpty() || !rawText.equals(historyInput.get(historyInput.size() - 1))) {
            historyInput.add(rawText);
        }
        historyIndex = historyInput.size();
    }
}

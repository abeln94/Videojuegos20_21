package com.unizar;

import com.unizar.game.Game;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

public class Main {
    public static String root = null;

    public static void main(String[] args) {

        final File currentDir = new File(".");
        final File[] folders = currentDir.listFiles(File::isDirectory);
        if (folders == null || folders.length == 0) {
            Utils.showMessage("No games", "No game folders were found. Make sure they are in the current directory.\nCurrent directory: " + currentDir.getAbsolutePath(), true);
            return;
        }

        final String[] roots = Arrays.stream(folders).map(File::getName).toArray(String[]::new);

        if (roots.length == 1) {
            root = roots[0];
        } else {
            root = (String) JOptionPane.showInputDialog(
                    null,
                    "Which game folder do you want to load?",
                    "Choose game",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    roots,
                    "hobbit"
            );
        }

        if (root == null) return;
        try {
            new Game();
        } catch (Throwable e) {
            e.printStackTrace();
            Utils.showMessage("Error", "Uh oh, can't start the game. The data wasn't found or may be corrupt (here is the error just in case):\n\n" + e, true);
            System.exit(-1);
        }
    }
}

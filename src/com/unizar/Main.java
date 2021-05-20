package com.unizar;

import com.unizar.game.Game;

public class Main {
    public static String root = "data";

    public static void main(String[] args) {
        if (args.length >= 1)
            root = args[0];

        try {
            new Game();
        } catch (Throwable e) {
            e.printStackTrace();
            Utils.showMessage("Error", "Uh oh, can't start the game. The data wasn't found or may be corrupt (here is the error just in case):\n\n" + e, true);
            System.exit(-1);
        }
    }
}

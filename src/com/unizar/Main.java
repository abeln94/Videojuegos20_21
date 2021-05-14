package com.unizar;

import com.unizar.game.Game;

public class Main {
    public static void main(String[] args) {
        try {
            new Game(args.length >= 1 ? args[0] : "data");
        } catch (Throwable e) {
            e.printStackTrace();
            Utils.showMessage("Error", "Uh oh, can't start the game. The data wasn't found or may be corrupt (here is the error just in case):\n\n" + e);
            System.exit(-1);
        }
    }
}

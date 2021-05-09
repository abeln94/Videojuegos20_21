package com.unizar;

import com.unizar.game.Game;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Game(args.length > 1 ? args[0] : "data");
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showMessage("Error", "Uh oh, can't start the game. The data wasn't found or may be corrupt:\n\n" + e.getMessage());
        }
    }
}

package com.unizar;

import com.unizar.game.Game;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Game(args.length > 1 ? args[0] : "data");
    }
}

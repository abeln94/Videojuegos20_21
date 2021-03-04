package com.unizar;

import com.unizar.hobbit.HobbitData;
import com.unizar.hobbit.Game;

public class Main {
    public static void main(String[] args) {
        new Game(new HobbitData()).start();
    }
}

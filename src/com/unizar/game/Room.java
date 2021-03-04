package com.unizar.game;

import com.unizar.hobbit.Game;

import java.io.Serializable;

abstract public class Room implements Serializable {

    transient protected Game game;

    abstract public void onEnter();

    abstract public String onCommand(String command);

    // internal

    public final void register(Game game) {
        this.game = game;
    }
}

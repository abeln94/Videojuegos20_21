package com.unizar.hobbit.rooms;

import com.unizar.game.Room;

public class StartScreen extends Room {
    @Override
    public void onEnter() {
        game.setImage("blue");
        game.addDescription("Pulsa enter para empezar");
        game.addOutput("Escribe aquí los comandos");
    }

    @Override
    public String onCommand(String command) {
        game.goToRoom("initial");
        return "";
    }
}

package com.unizar.hobbit.rooms;

import com.unizar.game.Room;

public class StartScreen extends Room {
    @Override
    public void onEnter() {
        game.setImage("1_español");
        game.addDescription("Bienvenido al juego. Pulsa Enter para empezar.");
    }

    @Override
    public String onCommand(String command) {
        game.goToRoom("initial");
        return "";
    }
}

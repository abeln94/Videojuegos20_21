package com.unizar.hobbit.rooms;

import com.unizar.game.Room;

public class StartScreen extends Room {
    @Override
    public void onEnter() {
        game.setImage("blue");
        game.addDescription("Bienvenido al juego, de momento solo hay 3 habitaciones. Pulsa Enter para empezar.");
    }

    @Override
    public String onCommand(String command) {
        game.goToRoom("initial");
        return "";
    }
}

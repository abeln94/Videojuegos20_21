package com.unizar.hobbit.rooms;

import com.unizar.game.Room;
import com.unizar.hobbit.Game;

public class NorthRoom extends Room {
    @Override
    public void onEnter() {
        Game.setImage("green");
        Game.addDescription("El bosque con animalitos, puedes volver");
    }

    @Override
    public String onCommand(String command) {
        switch (command){
            case "volver":
                Game.goToRoom("initial");
                return "Vuelves a la habitación";
        }
        return null;
    }
}

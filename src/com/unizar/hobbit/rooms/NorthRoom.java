package com.unizar.hobbit.rooms;

import com.unizar.game.Room;

public class NorthRoom extends Room {
    @Override
    public void onEnter() {
        game.setImage("3");
        game.addDescription("Las afueras, de momento solo puedes volver");
    }

    @Override
    public String onCommand(String command) {
        switch (command){
            case "volver":
                game.goToRoom("initial");
                return "Vuelves a la habitación";
        }
        return null;
    }
}

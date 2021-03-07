package com.unizar.hobbit.rooms;

import com.unizar.game.Room;

public class InitialRoom extends Room {

    @Override
    public void onEnter() {
        game.setImage("2");
        game.addDescription("La habitación inicial, de momento solo puedes salir");
    }

    @Override
    public String onCommand(String command) {
        switch (command){
            case "salir":
                game.goToRoom("north");
                return "Sales de la habitación";
        }
        return null;
    }
}

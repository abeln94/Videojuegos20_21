package com.unizar.hobbit.rooms;

import com.unizar.game.Room;

public class InitialRoom extends Room {

    @Override
    public void onEnter() {
        game.setImage("red");
        game.addDescription("La habitación inicial, puedes salir");
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

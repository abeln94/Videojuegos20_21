package com.unizar.hobbit.rooms;

import com.unizar.hobbit.Game;
import com.unizar.game.Room;

public class InitialRoom extends Room {

    @Override
    public void onEnter() {
        Game.setImage("red");
        Game.addDescription("La habitación inicial, puedes salir");
    }

    @Override
    public String onCommand(String command) {
        switch (command){
            case "salir":
                Game.goToRoom("north");
                return "Sales de la habitación";
        }
        return null;
    }
}

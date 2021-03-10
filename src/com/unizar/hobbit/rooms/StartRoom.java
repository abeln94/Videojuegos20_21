package com.unizar.hobbit.rooms;

import com.unizar.game.Room;

public class StartRoom extends Room {

    private boolean doorOpen = false;

    @Override
    public void onEnter() {
        game.setImage("2");
        game.addDescription("Te encuentras en una sala alargada confortable. Puedes ver:");
        game.addDescription("\tEl cofre de madera.");
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

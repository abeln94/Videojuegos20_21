package com.unizar.hobbit.rooms;

import com.unizar.hobbit.Game;
import com.unizar.game.Room;

public class InitialRoom extends Room {

    @Override
    public void onEnter() {
        Game.setImage("green");
        Game.addDescription("The starting room");
    }

    @Override
    public boolean onCommand(String command) {
        switch (command){
            case "go north":
                Game.goToRoom(new NorthRoom());
                return true;
        }
        return false;
    }
}

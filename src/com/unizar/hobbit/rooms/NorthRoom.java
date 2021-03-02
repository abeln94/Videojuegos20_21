package com.unizar.hobbit.rooms;

import com.unizar.game.Room;
import com.unizar.hobbit.Game;

public class NorthRoom extends Room {
    @Override
    public void onEnter() {
        Game.setImage("red");
        Game.addDescription("A beautiful room");
    }

    @Override
    public boolean onCommand(String command) {
        switch (command){
            case "go south":
                Game.goToRoom(new InitialRoom());
                return true;
        }
        return false;
    }
}

package com.unizar.hobbit;

import com.unizar.game.Room;
import com.unizar.game.Window;
import com.unizar.hobbit.rooms.InitialRoom;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Game implements Window.InputListener {
    static Window window = new Window("The hobbit");
    static Game game = new Game();

    static {
        window.setCommandListener(game);
    }

    static Room current = null;

    static public void start() {
        setImage("blue");
        addDescription("Press enter to start the game");
    }


    ///////////////////

    @Override
    public void onText(String text) {
        window.addOutput("> " + text);

        if (current == null) {
            // start game
            window.clearOutput();
            window.clearDescription();
            goToRoom(new InitialRoom());
            return;
        }

        if (!current.onCommand(text)) {
            window.addOutput("Unknown command");
        }
    }


    /////////////////
    public static void goToRoom(Room room) {
        current = room;
        window.clearDescription();
        room.onEnter();
    }

    static public void setImage(String image) {
        try {
            window.drawImage(ImageIO.read(Game.class.getResource("/images/" + image + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void addDescription(String description) {
        window.addDescription(description);
    }

}

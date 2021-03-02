package com.unizar.hobbit;

import com.unizar.game.Room;
import com.unizar.game.Window;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Game implements Window.InputListener {
    static Window window = new Window("El hobbit");
    static Game game = new Game();

    static {
        window.setCommandListener(game);
    }

    static Room current = null;

    static public void start() {
        setImage("blue");
        addDescription("Pulsa enter para empezar");
    }


    ///////////////////

    @Override
    public void onText(String text) {
        window.addOutput("> " + text);

        if (current == null) {
            // start game
            window.clearOutput();
            window.clearDescription();
            goToRoom("initial");
            return;
        }

        String result = current.onCommand(text);
        window.addOutput(result == null ? "No se como '" + text+"'" : result);
    }


    /////////////////
    public static void goToRoom(String room) {
        current = Elements.getRoom(room);
        window.clearDescription();
        current.onEnter();
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

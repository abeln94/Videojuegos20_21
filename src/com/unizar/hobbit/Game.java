package com.unizar.hobbit;

import com.unizar.game.Data;
import com.unizar.game.DataSaver;
import com.unizar.game.Room;
import com.unizar.game.Window;

import javax.imageio.ImageIO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Game extends KeyAdapter implements Window.InputListener {
    private Data data;
    private final Window window;

    private final DataSaver saver = new DataSaver();

    public Game(Data data) {
        this.data = data;
        data.register(this);
        window = new Window(data.getTitle());
        window.setCommandListener(this);
        window.setKeyListener(this);
    }

    public void start() {
        goToRoom(data.getCurrentRoom());
    }

    ///////////////////

    @Override
    public void onText(String text) {
        if (!text.isEmpty()) window.addOutput("> " + text);


        String result = data.getRoom(data.getCurrentRoom()).onCommand(text);
        window.addOutput(result == null ? "No se como '" + text + "'" : result);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F6 -> {
                window.addOutput("[saved]");
                saver.saveData(data);
            }
            case KeyEvent.VK_F9 -> {
                window.clearOutput();
                window.addOutput("[loaded]");
                Data newData = saver.loadData();
                if (newData != null) {
                    data = newData;
                    data.register(this);
                    goToRoom(data.getCurrentRoom());
                }
            }
        }
    }

    /////////////////

    public void goToRoom(String room) {
        Room current = data.getRoom(room);
        data.setCurrentRoom(room);
        window.clearDescription();
        current.onEnter();
    }

    public void setImage(String image) {
        try {
            window.drawImage(ImageIO.read(Game.class.getResource("/images/" + image + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDescription(String description) {
        window.addDescription(description);
    }

    public void addOutput(String output) {
        window.addOutput(output);
    }

}

package com.unizar.game;

import javax.imageio.ImageIO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * The game main class.
 */
public class Game extends KeyAdapter implements Window.InputListener {

    // ------------------------- variables -------------------------
    private Data data;
    private final Window window;
    private final DataSaver saver = new DataSaver();

    // ------------------------- initializers -------------------------

    /**
     * Initializer
     *
     * @param data data of the game to create
     */
    public Game(Data data) {
        this.data = data;
        window = new Window(data.getTitle(), data.getImageRatio(), data.getFontName());
        window.setCommandListener(this);
        window.setKeyListener(this);
        reset();
    }

    /**
     * Resets the game
     */
    public void reset() {
        // recreate the data object
        try {
            data = data.getClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // should never happen
            e.printStackTrace();
            System.exit(-1);
        }
        data.register(this);

        // restart
        window.clearOutput();
        window.clearDescription();
        goToRoom(data.getCurrentRoom());
        addOutput("Escribe aquí los comandos y pulsa enter para introducirlos.");
        addOutput("También puedes pulsar F6/F9 para guardar/cargar la partida. Y pulsar F2 para resetear.");
    }

    // ------------------------- listeners -------------------------

    @Override
    public void onText(String text) {
        if (!text.isEmpty()) window.addOutput("> " + text);

        // perform command
        // TODO: replace with a command manager
        String result = data.getRoom(data.getCurrentRoom()).onCommand(text);
        if (result == null) {
            // invalid command
            window.addOutput("No se como '" + text + "'");
            return;
        }

        // player action
        window.addOutput(result);

        // elements actions
        data.act();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

            // press F6 to save
            case KeyEvent.VK_F6 -> {
                window.addOutput("[guardado]");
                saver.saveData(data);
            }

            // Press F9 to load
            case KeyEvent.VK_F9 -> {
                Data newData = saver.loadData();
                if (newData != null) {
                    window.clearOutput();
                    window.addOutput("[cargado]");
                    data = newData;
                    data.register(this);
                    goToRoom(data.getCurrentRoom());
                } else {
                    window.addOutput("[No hay datos guardados]");
                }
            }

            // Press F2 to reset
            case KeyEvent.VK_F2 -> reset();
        }
    }

    // ------------------------- game commands -------------------------

    /**
     * Goes to another room
     *
     * @param room which room to go to
     */
    public void goToRoom(String room) {
        Room current = data.getRoom(room);
        data.setCurrentRoom(room);
        window.clearDescription();
        current.onEnter();
    }

    /**
     * Sets an image
     *
     * @param label name of the image (from the images folder)
     */
    public void setImage(String label) {
        try {
            window.drawImage(ImageIO.read(Game.class.getResource(data.getImagePath(label))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a description line
     *
     * @param description text to add
     */
    public void addDescription(String description) {
        window.addDescription(description);
    }

    /**
     * Adds a command output
     *
     * @param output text to add
     */
    public void addOutput(String output) {
        window.addOutput(output);
    }

}

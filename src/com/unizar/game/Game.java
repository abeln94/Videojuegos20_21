package com.unizar.game;

import com.unizar.game.commands.Parser;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.Player;

import javax.imageio.ImageIO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * The game main class.
 */
public class Game extends KeyAdapter implements Window.InputListener {

    // ------------------------- global -------------------------
    public Data data;
    public Engine engine = new Engine();

    private final DataSaver saver = new DataSaver();
    private final Parser analyzer = new Parser(this);
    public final Window window;

    private boolean onStartScreen = true;

    // ------------------------- initializers -------------------------

    /**
     * Initializer
     *
     * @param data data of the game to create
     */
    public Game(Data data) {
        this.data = data;
        window = new Window(data.properties.getTitle(), data.properties.getImageRatio(), data.properties.getFontName());
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
        data.init();

        // restart
        onStartScreen = true;
        setImage(data.properties.getStartScreen());
        window.clearOutput();
        addOutput("Escribe aquí los comandos y pulsa enter para introducirlos.");
        addOutput("También puedes pulsar F6/F9 para guardar/cargar la partida. Y pulsar F2 para resetear.");
        window.clearDescription();
        addDescription(data.properties.getDescription());
        addDescription("Pulsa cualquier tecla para empezar.");
    }

    // ------------------------- listeners -------------------------

    @Override
    public void onText(String text) {
        // analyze command
        analyzer.onText(text);

        update();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (onStartScreen) {
            // any key while on the start screen
            onStartScreen = false;
            window.clearCommand();
            update();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (onStartScreen) return;

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
                    update();
                } else {
                    window.addOutput("[No hay datos guardados]");
                }
            }

            // Press F2 to reset
            case KeyEvent.VK_F2 -> reset();
        }
    }

    // ------------------------- game commands -------------------------

    public void update() {
        window.clearDescription();

        // describe current room
        Element location = getPlayer().location;
        setImage(location instanceof Location ? ((Location) location).image : null);
        addDescription("Te encuentras en " + location.getDescription(getPlayer()) + ".");
    }

    /**
     * Sets an image
     *
     * @param label name of the image (from the images folder)
     */
    public void setImage(String label) {
        if (label == null) {
            window.drawImage(null);
        } else {
            try {
                window.drawImage(ImageIO.read(Game.class.getResource(data.properties.getImagePath(label))));
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    /**
     * Returns the element associated with the given class (should be unique)
     */
    public <T> T findElementByClassName(Class<T> name) {
        return (T) data.elements.stream()
                .filter(name::isInstance).findFirst().orElseThrow();
    }

    public Player getPlayer() {
        return findElementByClassName(Player.class);
    }

    public void afterPlayer() {
        // act each element
        data.elements.forEach(Element::act);
    }

}

package com.unizar.game;

import com.unizar.game.commands.Engine;
import com.unizar.game.commands.Parser;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.Player;

import javax.imageio.ImageIO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.stream.Collectors;

/**
 * The game main class.
 */
public class Game extends KeyAdapter {

    // ------------------------- global -------------------------
    public World world;
    public Engine engine = new Engine();

    private final DataSaver saver = new DataSaver();
    public final Parser parser = new Parser(this);
    public final Window window;

    // ------------------------- local -------------------------

    private enum State {
        StartScreen,
        Playing,
        GameOverScreen,
        WinScreen,
    }

    private State state;

    // ------------------------- initializers -------------------------

    /**
     * Initializer
     *
     * @param world data of the game to create
     */
    public Game(World world) {
        this.world = world;
        window = new Window(world.properties.getTitle(), world.properties.getImageRatio(), world.properties.getFontName());
        window.setKeyListener(this);
        startScreen();
    }

    /**
     * Start the game
     */
    public void startScreen() {
        state = State.StartScreen;
        reset();
        setImage(world.properties.getStartScreen());
        window.clearOutput();
        addOutput("Escribe aquí los comandos y pulsa enter para introducirlos.");
        addOutput("También puedes pulsar F6/F9 para guardar/cargar la partida. Y pulsar F2 para resetear.");
        window.clearDescription();
        addDescription(world.properties.getStartDescription());
        addDescription("");
        addDescription("Pulsa cualquier tecla para empezar.");
    }

    public void winScreen() {
        state = State.WinScreen;
        setImage(world.properties.getWinScreen());
        window.clearOutput();
        addOutput("Enhorabuena, has completado el juego.");
        window.clearDescription();
        addDescription(world.properties.getWinDescription());
        addDescription("");
        addDescription(getCompletion());
        addDescription("");
        addDescription("Pulsa cualquier tecla para volver a empezar.");
    }

    /**
     * Resets the game
     */
    public void reset() {
        // recreate the data object
        try {
            world = world.getClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // should never happen
            e.printStackTrace();
            System.exit(-1);
        }
        world.register(this);
        world.init();
    }

    // ------------------------- listeners -------------------------

    @Override
    public void keyReleased(KeyEvent e) {
        switch (state) {
            case StartScreen -> {
                state = State.Playing;
                window.clearCommand();
                update();
            }
            case Playing -> {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // analyze command
                    final String command = window.getCommand();
                    window.clearCommand();
                    parser.onText(command);
                }
            }
            case GameOverScreen, WinScreen -> {
                window.clearCommand();
                startScreen();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (state != State.Playing) return;

        switch (e.getKeyCode()) {

            // press F6 to save
            case KeyEvent.VK_F6 -> {
                window.addOutput("[guardado]");
                saver.saveData(world);
            }

            // Press F9 to load
            case KeyEvent.VK_F9 -> {
                World newWorld = saver.loadData();
                if (newWorld != null) {
                    window.clearOutput();
                    window.addOutput("[cargado]");
                    world = newWorld;
                    world.register(this);
                    update();
                    parser.clearHistory();
                } else {
                    window.addOutput("[No hay datos guardados]");
                }
            }

            // Press top arrow to repeat input
            case KeyEvent.VK_UP -> parser.restoreInput(true);
            case KeyEvent.VK_DOWN -> parser.restoreInput(false);

            // Press F2 to reset
            case KeyEvent.VK_F2 -> {
                startScreen();
                parser.clearHistory();
            }
        }
    }

    public void afterPlayer() {
        // act each element
        world.elements.forEach(Element::act);

        // update objectives
        world.requiredObjectives = world.requiredObjectives.stream().filter(p -> !p.second.apply(this)).collect(Collectors.toList());
        world.optionalObjectives = world.optionalObjectives.stream().filter(p -> !p.apply(this)).collect(Collectors.toList());

        // check finalization
        if (world.requiredObjectives.isEmpty()) {
            // win the game
            winScreen();
        } else {
            // still playing
            update();
            world.act();
        }
    }

    // ------------------------- game commands -------------------------

    public void update() {
        window.clearDescription();

        // describe current room
        Element location = getPlayer().location;
        setImage(location instanceof Location ? ((Location) location).image : null);
        addDescription("Te encuentras en " + location.getDescription(getPlayer()) + "F"); //TODO: include the dots inside the description, not outside
        addDescription("");
        addDescription(getPlayer().getDescription(getPlayer()) + ".");
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
                final String path = world.properties.getImagePath(label);
                final URL resource = Game.class.getResource(path);
                if (resource == null) {
                    throw new IOException("The image '" + path + "' doesn't exist.");
                }
                window.drawImage(ImageIO.read(resource));
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
        if (output.isEmpty()) return;
        window.addOutput(output);
    }

    /**
     * Returns the element associated with the given class (should be unique)
     */
    public <T> T findElementByClassName(Class<T> name) {
        return (T) world.elements.stream()
                .filter(name::isInstance).findFirst().orElseThrow();
    }

    public Player getPlayer() {
        return findElementByClassName(Player.class);
    }

    public String getCompletion() {
        final int percentage = 100 - 100 * (world.requiredObjectives.size() + world.optionalObjectives.size()) / world.totalObjectives;
        return "Has completado el " + percentage + "% de tu aventura.";
    }
}

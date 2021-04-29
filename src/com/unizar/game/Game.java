package com.unizar.game;

import com.unizar.Utils;
import com.unizar.game.commands.*;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;
import com.unizar.game.elements.Player;

import javax.imageio.ImageIO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * The game main class.
 */
public class Game extends KeyAdapter {

    // ------------------------- global -------------------------
    public World world;

    private final DataSaver saver = new DataSaver();
    public final History history = new History(this);
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
        addOutput("También puedes pulsar F6/F9 para guardar/cargar la partida. Y pulsar F2 para resetear. Si necesitas ayuda sobre el juego puedes pulsar F1.");
        window.clearDescription();
        addDescription(world.properties.getStartDescription());
        addDescription("");
        addDescription("Pulsa cualquier tecla para empezar.");
    }

    /**
     * Wins the game
     */
    public void winScreen() {
        state = State.WinScreen;
        setImage(world.properties.getWinScreen());
        addOutput("Enhorabuena, has completado el juego.");
        window.clearDescription();
        addDescription(world.properties.getWinDescription());
        addDescription("");
//        addDescription(getCompletion());
//        addDescription("");
        addDescription("Pulsa cualquier tecla para volver a empezar.");
    }

    /**
     * Game overs the game
     */
    public void gameOverScreen() {
        state = State.GameOverScreen;
        setImage(null);
        addOutput("Has muerto.");
        window.clearDescription();
        addDescription("Estás muerto.");
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
    public void keyPressed(KeyEvent e) {
        switch (state) {
            case StartScreen:
                state = State.Playing;
                update();
                return;
            case Playing:
                break;
            case GameOverScreen:
            case WinScreen:
                window.clearCommand();
                startScreen();
                return;
        }

        switch (e.getKeyCode()) {

            // press enter to perform command
            case KeyEvent.VK_ENTER:
                window.disableInput();
                new Thread(() -> {
                    String command = window.getCommand();
                    window.clearCommand();
                    run(command);
                    window.enableInput();
                }).start();
                break;

            // press F6 to save
            case KeyEvent.VK_F6:
                window.addOutput("[guardado]");
                saver.saveData(world);
                break;

            // Press F9 to load
            case KeyEvent.VK_F9:
                World newWorld = saver.loadData();
                if (newWorld != null) {
                    window.clearOutput();
                    window.addOutput("[cargado]");
                    world = newWorld;
                    world.register(this);
                    update();
                    history.clearHistory();
                } else {
                    window.addOutput("[No hay datos guardados]");
                }
                break;

            // Press top arrow to repeat input
            case KeyEvent.VK_UP:
                history.restoreInput(true);
                break;
            case KeyEvent.VK_DOWN:
                history.restoreInput(false);
                break;

            // Press F2 to reset
            case KeyEvent.VK_F2:
                startScreen();
                history.clearHistory();
                break;

            // press F1 for help
            case KeyEvent.VK_F1:
                Utils.showMessage("Ayuda", "/raw/help.txt");
                break;

            // press F12 for debug
            case KeyEvent.VK_F12:
                Debug.teleportPlayer(this);
                update();
                break;
        }
    }

    public void run(String rawText) { // TODO: move to engine?
        // skip empty lines
        if (rawText.isEmpty()) return;

        // write command
        addOutput("> " + rawText);

        try {
            Command command = Command.parse(rawText, world.elements).asNPC(getPlayer());

            history.add(rawText);

            // execute
            Result result = Engine.execute(command);

            // add output
            if (result.done) {
                addOutput(result.output);
            } else {
                if (result.requiresMore != null)
                    throw new EngineException(result.output, rawText + " " + result.requiresMore);
                else throw new EngineException(result.output);
            }

            // player end

            // act each npc
            world.elements.stream().filter(e -> e instanceof NPC).forEach(Element::act);

            // update objectives
            world.elements.forEach(element -> element.pendingObjectives = element.pendingObjectives.stream().filter(objective -> !objective.isCompleted()).collect(Collectors.toSet()));

            // check death
            if (getPlayer().getLocation() == null) {
                gameOverScreen();
                return;
            }

            // check finalization
            if (world.playerWon(this)) {
                winScreen();
                return;
            }

            // new turn

            // act each non-NPC
            world.act();
            world.elements.stream().filter(e -> !(e instanceof NPC)).forEach(Element::act);

            // update window
            update();

            // wait again for player

        } catch (EngineException e) {
            if (e.userError != null) addOutput(e.userError);
            if (e.newUserInput != null) window.setCommand(e.newUserInput);
        }

    }

    // ------------------------- game commands -------------------------

    public void update() {
        window.clearDescription();

        // describe current room
        Element location = getPlayer().getLocation();
        setImage(location instanceof Location ? ((Location) location).image : null);
        addDescription("Te encuentras en " + location.getDescription());
        addDescription("");
        addDescription(getPlayer().getDescription());
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

        // spanish is difficult
        output = output.replaceAll("\\ba el\\b", "al");
        output = output.replaceAll("\\bde el\\b", "del");

        window.addOutput(output);
    }

    /**
     * Returns the element associated with the given class (should be unique)
     */
    public <T> T findElementByClassName(Class<T> name) {
        return (T) world.elements.stream()
                .filter(name::isInstance).findFirst().orElseThrow(() -> new NoSuchElementException("Element " + name + " not found. Did you forgot to register it in the World?"));
    }

    public Player getPlayer() {
        return findElementByClassName(Player.class);
    }

    public String getCompletion() {
        double percentage = world.elements.stream()
                .filter(element -> element.totalObjectives != 0)
                .mapToInt(element -> 100 - 100 * element.pendingObjectives.size() / element.totalObjectives)
                .average().orElse(0);

        return "Has completado el " + percentage + "% de tu aventura.";
    }
}

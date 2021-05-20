package com.unizar.game;

import com.unizar.Utils;
import com.unizar.game.commands.*;
import com.unizar.game.elements.Element;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;
import com.unizar.game.elements.Player;
import com.unizar.generic.JSONWorld;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The game main class.
 */
public class Game extends KeyAdapter implements Runnable {

    // ------------------------- global -------------------------
    public World world;
    private final String root;
    public Engine engine = new Engine();
    public Sound sound = new Sound();
    public Scheduling autoWait = new Scheduling(this, 10 * 1000);

    private final DataSaver saver = new DataSaver();
    public final History history = new History();
    public final Window window;

    // ------------------------- local -------------------------

    private enum State {
        StartScreen,
        Playing,
        GameOverScreen,
        WinScreen,
        Pause
    }

    private State state;

    // ------------------------- initializers -------------------------

    /**
     * Initializer
     *
     * @param root root folder of the data
     */
    public Game(String root) throws IOException {
        this.root = root;
        this.world = new JSONWorld(root);
        window = new Window(world.properties.getTitle(), world.properties.getImageRatio(), world.properties.getFontFile());
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
        setMusic(world.properties.getStartMusic());
        window.clearOutput();
        addOutput("Escribe aquí los comandos y pulsa enter para introducirlos.");
        addOutput("También puedes pulsar F6/F9 para guardar/cargar la partida. Y pulsar F2 para resetear. Si necesitas ayuda sobre el juego puedes pulsar F1.");
        window.clearDescription();
        setDescription(
                world.properties.getStartDescription(),
                "",
                "Pulsa cualquier tecla para empezar."
        );
    }

    /**
     * Wins the game
     */
    public void winScreen() {
        state = State.WinScreen;
        setImage(world.properties.getWinScreen());
        setMusic(world.properties.getWinMusic());
        addOutput("Enhorabuena, has completado el juego.");
        window.clearDescription();
        setDescription(
                world.properties.getWinDescription(),
                "",
                getCompletion(),
                "",
                "Pulsa cualquier tecla para volver a empezar."
        );
    }

    /**
     * Game overs the game
     */
    public void gameOverScreen() {
        state = State.GameOverScreen;
        setImage(world.properties.getGameOverScreen());
        setMusic(world.properties.getGameOverMusic());
        addOutput("Has muerto.");
        window.clearDescription();
        setDescription(
                "Estás muerto.",
                "",
                getCompletion(),
                "",
                "Pulsa cualquier tecla para volver a empezar."
        );
    }

    public void pause() {
        // pause
        state = State.Pause;
        autoWait.cancel();
        addOutput("Juego pausado. Pulsa cualquier tecla para continuar...");
    }

    /**
     * Resets the game
     */
    public void reset() {
        // reset utils
        autoWait.cancel();
        sound.stop();
        window.disableInput();

        // recreate the data object
        try {
            world = world.getClass().getConstructor(String.class).newInstance(root);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Utils.showMessage("Error", "Uh oh, can't reload the game. The data wasn't found or may be corrupt (here is the error just in case):\n\n" + e, true);
        }
        world.register(this);
        world.init();
    }

    private boolean running = false;

    private void backgroundRun(Runnable runnable) {
        if (running) return;
        running = true;
        window.disableInput();
        new Thread(() -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                addOutput("{Internal error: " + throwable + "}");
            } finally {
                running = false;
                if (state == State.Playing)
                    window.enableInput();
            }
        }).start();
    }

    // ------------------------- listeners -------------------------

    /**
     * Called periodically to run the 'wait' command
     */
    @Override
    public void run() {
        backgroundRun(() -> run("esperar"));
    }

    /**
     * Called when the user presses any key in the input box
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Press F9 to load
            case KeyEvent.VK_F9:
                load();
                return;

            // Press F2 to reset
            case KeyEvent.VK_F2:
                startScreen();
                history.clearHistory();
                return;

            // press F1 for help
            case KeyEvent.VK_F1:
                backgroundRun(this::help);
                return;
        }

        switch (state) {
            case Pause:
                window.addOutput("...el juego continúa.\n");
            case StartScreen:
                state = State.Playing;
                backgroundRun(() -> {
                    update();
                    autoWait.schedule();
                });
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

            // press pause to pause
            case KeyEvent.VK_PAUSE:
                backgroundRun(this::pause);
                break;

            // press enter to perform command
            case KeyEvent.VK_ENTER:
                backgroundRun(() -> {
                    String command = window.getCommand();
                    window.clearCommand();
                    history.add(command);
                    run(command);
                });
                break;

            // press F6 to save
            case KeyEvent.VK_F6:
                save();
                break;

            // Press top arrow to repeat input
            case KeyEvent.VK_UP:
                window.setCommand(history.getPreviousInput(true));
                break;
            case KeyEvent.VK_DOWN:
                window.setCommand(history.getPreviousInput(false));
                break;

            // Press F2 to reset
            case KeyEvent.VK_F2:
                startScreen();
                history.clearHistory();
                break;

            // press F12 for debug
            case KeyEvent.VK_F12:
                backgroundRun(() -> {
                    if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0) {
                        Utils.showMessage("http://viz-js.com/", ElementSearcher.generateGraph(world.elements), true);
                    } else {
                        Debug.teleportPlayer(this);
                        update();
                    }
                });
                break;
        }
    }

    public void run(String rawText) { // TODO: move to engine?
        // skip empty lines
        if (rawText.isEmpty()) return;

        // stop scheduling
        autoWait.cancel();

        // write command
        addOutput("\n> " + rawText);

        try {
            Command command = Command.parse(rawText, world.elements);

            if (command.action == Word.Action.PAUSE) {
                pause();
                return;
            }

            // execute
            Result result = engine.execute(getPlayer(), command);

            // add output
            if (result.done) {
                addOutput(result.output);
            } else {
                throw new EngineException(result.output, result.requiresMore);
            }

            if (command.action == Word.Action.GO) {
                forceImageUpdate = true;
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
            if (e.userError != null)
                addOutput(e.userError);
            if (e.newUserInput != null)
                window.setCommand(history.getPreviousInput(true) + " " + e.newUserInput);
        }

        // schedule next wait command
        autoWait.schedule();

    }

    // ------------------------- game commands -------------------------

    private String lastImage = null;
    private boolean forceImageUpdate = false;

    /**
     * Updates the ui with the new image/sound/description
     */
    public void update() {
        window.clearDescription();

        Element location = getPlayer().getLocation();
        final String image = location instanceof Location ? ((Location) location).getImage() : null;
        final String music = location instanceof Location ? ((Location) location).getMusic() : null;

        if (!Objects.equals(lastImage, image) || forceImageUpdate) {
            forceImageUpdate = false;

            // smooth update image
            Utils.smoothing(f -> {
                window.setImageTransparency(1 - f);
            });

            setMusic(music);
            setImage(image);

            Utils.smoothing(window::setImageTransparency);
        }

        // describe current room
        setDescription(
                "Te encuentras en " + location.getDescription(),
                "",
                getPlayer().getDescription()
        );
    }

    /**
     * Sets an image
     *
     * @param label name of the image (from the images folder)
     */
    public void setImage(String label) {
        window.drawImage(label == null ? null : world.properties.getImagePath(label));
        lastImage = label;
    }

    /**
     * Sets a music
     *
     * @param label name of the image (from the musics folder)
     */
    public void setMusic(String label) {
        sound.backgroundMusic(label == null ? null : world.properties.getMusicPath(label));
    }

    /**
     * Adds a description text
     *
     * @param description each description will be on a different line
     */
    public void setDescription(String... description) {
        window.setDescription(String.join("\n", description));
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
                .filter(name::isInstance)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Element " + name + " not found. Did you forgot to register it in the World?"));
    }

    /**
     * Returns the elements associated with the given class
     */
    public <T> List<T> findElementsByClassName(Class<T> name) {
        return (List<T>) world.elements.stream()
                .filter(name::isInstance)
                .collect(Collectors.toList());
    }

    /**
     * @return the player element
     */
    public Player getPlayer() {
        return findElementByClassName(Player.class);
    }

    /**
     * @return the completion string
     */
    public String getCompletion() {
        double percentage = world.elements.stream()
                .filter(element -> element.totalObjectives != 0)
                .mapToInt(element -> 100 - 100 * element.pendingObjectives.size() / element.totalObjectives)
                .average().orElse(0);

        return String.format("Has completado el %.2f %% de tu aventura.", percentage);
    }


    /**
     * Saves the game
     */
    public void save() {
        window.addOutput("[guardado]");
        saver.saveData(world);
    }

    /**
     * Loads the game
     */
    public void load() {
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
    }

    /**
     * Exits the game
     */
    public void exit() {
        window.close();
    }

    /**
     * Shows the help file
     */
    public void help() {

        // engine help
        InputStream resource = Game.class.getResourceAsStream("/help.txt");
        assert resource != null;
        String engineHelp = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining("\n"));

        // game help
        String gameHelp;
        try {
            gameHelp = Utils.readFile(world.properties.getHelpFile());
        } catch (IOException ioException) {
            gameHelp = ioException.toString();
        }

        Utils.showMessage("Ayuda", (gameHelp != null ? gameHelp + "\n\n\n\n\n--------------------------------------------------\n\n\n\n\n" : "") + engineHelp, false);
    }
}

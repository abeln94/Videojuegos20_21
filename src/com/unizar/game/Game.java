package com.unizar.game;

import com.unizar.game.commands.Parser;
import com.unizar.game.commands.Word;
import com.unizar.game.elements.*;

import javax.imageio.ImageIO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The game main class.
 */
public class Game extends KeyAdapter implements Window.InputListener {

    // ------------------------- global -------------------------
    public Data data;

    private final DataSaver saver = new DataSaver();
    private final Parser analyzer = new Parser(this);
    private final Window window;

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
        Element location = getElement(getPlayer().location);
        setImage(location instanceof Location ? ((Location) location).image : null);
        addDescription("Te encuentras en " + location.getDescription(getPlayer().getClass()) + ".");
    }

    /**
     * Sets an image
     *
     * @param label name of the image (from the images folder)
     */
    public void setImage(String label) {
        try {
            window.drawImage(ImageIO.read(Game.class.getResource(data.properties.getImagePath(label))));
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

    public <T> List<T> getElements(Class<T> name) {
        return (List<T>) data.elements.stream()
                .filter(name::isInstance)
                .collect(Collectors.toList());
    }

    /**
     * Returns the first element associated with the given class
     */
    public <T> T getElement(Class<T> name) {
        return getElements(name).stream().findFirst().orElseThrow();
    }

    public Player getPlayer() {
        return getElement(Player.class);
    }

    public void afterPlayer() {
        // foreach NPC, npc.act
    }


    // ------------------------- Engine -------------------------

    public static class Result {
        public boolean done = false;
        public boolean requiresMore = false;
        public String output = null;

        static Result done(String output) {
            Result result = new Result();
            result.done = true;
            result.output = output;
            return result;
        }

        static Result moreNeeded(String output) {
            Result result = new Result();
            result.requiresMore = true;
            result.output = output;
            return result;
        }

        static Result error(String output) {
            Result result = new Result();
            result.output = output;
            return result;
        }
    }

    public Result applyCommand(Class<? extends NPC> npc, Word.Adverbs adverb, Word.Action action, Word.Preposition preposition, Word.Direction direction, Class<? extends Element> element) {
        System.out.println(npc + " - " + adverb + "  - " + action + " - " + preposition + " - " + direction + " - " + element);

        if (action == null) {
            return Result.error("Como dices?");
        }


        switch (action) {
            case OPEN -> {
                if (element == null) return Result.moreNeeded("Que quieres que abra?");

                Element el = getElement(element);

                if (!(el instanceof Item) || ((Item) el).opened == null) {
                    return Result.error("No puedo abrir " + el.name);
                } else if (((Item) el).opened) {
                    return Result.error(el.name + " ya está abierto/a");
                } else {
                    ((Item) el).opened = true;
                    return Result.done("Abres " + el.name);
                }
            }
            case CLOSE -> {
                if (element == null) return Result.moreNeeded("Que quieres que cierre?");

                Element el = getElement(element);

                if (!(el instanceof Item) || ((Item) el).opened == null) {
                    return Result.error("No puedo cerrar " + el.name);
                } else if (!((Item) el).opened) {
                    return Result.error(el.name + " ya está cerrado/a");
                } else {
                    ((Item) el).opened = false;
                    return Result.done("Cierras " + el.name);
                }
            }
            case GO -> {
                if (direction == null) {
                    // original game makes instead a 'go through'
                    return Result.moreNeeded("Hacia donde quieres que vaya?");
                }

                Element location = getElement(getElement(npc).location);

                if (!(location instanceof Location)) {
                    return Result.error("No puedes moverte mientras estás en " + location.name);
                }

                Utils.Pair<Class<? extends Location>, Class<? extends Item>> le = ((Location) location).exits.get(direction);

                if (le == null) {
                    return Result.error("No puedes ir hacia el " + direction.name);
                }

                Class<? extends Location> newLocation = le.first;
                Class<? extends Item> throughItem = le.second;

                if (throughItem != null && !getElement(throughItem).opened) {
                    return Result.error(getElement(throughItem).name + " está cerrado/a");
                }

                getElement(npc).changeLocation(newLocation);
                return Result.done("Te diriges al " + direction.name);

            }
        }


        return Result.error("Aún no se hacer eso!");
    }

}

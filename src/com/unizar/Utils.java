package com.unizar;

import com.unizar.game.Game;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * That utils class
 */
public class Utils {

    /**
     * Joins the strings of a list into a string using commas and 'y'
     *
     * @param empty          if the list is empty, this is returned.
     * @param prefixOne      prefix if the list contains one item only
     * @param prefixMultiple prefix if the list contains 2+ items
     * @param list           list of strings
     * @return "empty" or "prefixOne el1" or "prefixMultiple el1 y el2" or "prefixMultiple el1, el2 y el3" etc
     */
    static public String joinList(String empty, String prefixOne, String prefixMultiple, List<String> list) {
        // empty
        if (list.isEmpty()) return empty;

        // one
        if (list.size() == 1) return prefixOne + " " + list.get(0) + ".";

        // 2+
        int last = list.size() - 1;
        return prefixMultiple + " " + String.join(" y ", String.join(", ", list.subList(0, last)), list.get(last)) + ".";
    }

    // ------------------------- -------------------------

    public static Random random = new Random();

    /**
     * Picks a random element from an array
     */
    static public <T> T pickRandom(T[] array) {
        return array[random.nextInt(array.length)];
    }

    /**
     * Show a dialog with a scrollable window with content from a file
     *
     * @param title window title
     * @param path  path to the file with the message text
     */
    public static void showMessage(String title, String path) {
        // read message
        final InputStream resource = Game.class.getResourceAsStream(path);
        if (resource == null) {
            throw new RuntimeException("The file '" + path + "' doesn't exist.");
        }
        final String message = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining("\n"));

        // create window
        JTextArea textArea = new JTextArea(25, 50);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setText(message);
        textArea.setCaretPosition(0); // to start at the top
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // show
        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Increases the left padding of each line of a string
     *
     * @param string string content
     * @return the same string, but each line has an additional 4 spaces before
     */
    public static String increasePadding(String string) {
        return Arrays.stream(string.split("\n"))
                .map(line -> "    " + line)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Pauses the thread
     *
     * @param milliseconds time to pause
     */
    public static void pause(int milliseconds) {
        synchronized (new Object()) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static final int TIME = 250;
    private static final int STEPS = 50;

    /**
     * Runs the function with the parameter bewteen 0 and 1
     *
     * @param function input: 0-1 float
     */
    public static void smoothing(Consumer<Float> function) {
        for (int step = 0; step < STEPS; ++step) {
            function.accept((float) step / (STEPS - 1));
            pause(TIME / STEPS);
        }
    }

    // ------------------------- -------------------------

    /**
     * A container of 2 elements
     */
    static public class Pair<A, B> implements Serializable {
        public A first;
        public B second;

        static public <A, B> Pair<A, B> of(A a, B b) {
            Pair<A, B> p = new Pair<>();
            p.first = a;
            p.second = b;
            return p;
        }
    }
}

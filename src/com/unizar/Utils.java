package com.unizar;

import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
        if (array.length == 0) return null;
        else return array[random.nextInt(array.length)];
    }

    /**
     * Picks a random element from a set
     */
    public static <T> T pickRandom(Set<T> attackNPCs) {
        return (T) pickRandom(attackNPCs.toArray());
    }

    /**
     * Picks a random element from a list
     */
    public static <T> T pickRandom(List<T> interactable) {
        return (T) pickRandom(interactable.toArray());
    }

    /**
     * Returns a random parameter based on the weights
     *
     * @param parameters list of things+weights
     * @return a thing (or null if all weights are 0)
     */
    public static <T> T pickWeightedRandom(Set<Pair<T, Integer>> parameters) {
        if (parameters.isEmpty()) return null;

        // pick random from total weight
        final int sum = parameters.stream().mapToInt(p -> p.second).sum();
        int picked = random.nextInt(sum);

        // get based on weight
        for (Pair<T, Integer> parameter : parameters) {
            picked -= parameter.second;
            // picked
            if (picked < 0) return parameter.first;
        }
        // should never happen
        return null;
    }

    /**
     * Show a dialog with a scrollable window with content from a file
     *
     * @param title   window title
     * @param message message text
     */
    public static void showMessage(String title, String message) {
        // read message

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

    /**
     * Reads a file and returns its content
     *
     * @param path file path
     * @return the content as string
     * @throws IOException if can't read file
     */
    public static String readFile(String path) throws IOException {
        return String.join("\n", Files.readAllLines(Paths.get(path)));
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

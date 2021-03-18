package com.unizar;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

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
        if (list.size() == 1) return prefixOne + " " + list.get(0);

        // 2+
        int last = list.size() - 1;
        return prefixMultiple + " " + String.join(" y ", String.join(", ", list.subList(0, last)), list.get(last));
    }

    // ------------------------- -------------------------

    static Random random = new Random();

    /**
     * Picks a random element from an array
     */
    static public <T> T pickRandom(T[] array) {
        return array[random.nextInt(array.length)];
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

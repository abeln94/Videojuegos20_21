package com.unizar;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Utils {
    static public String generateList(String empty, String prefixOne, String prefixMultiple, List<String> list) {
        // empty
        if (list.isEmpty()) return empty;

        // one
        if (list.size() == 1) return prefixOne + list.get(0);

        // 2+
        int last = list.size() - 1;
        return prefixMultiple + String.join(" y ", String.join(", ", list.subList(0, last)), list.get(last));
    }

    static Random random = new Random();

    static public <T> T pickRandom(T[] array) {
        return array[random.nextInt(array.length)];
    }

    static public class Pair<A, B> implements Serializable {
        public A first;
        public B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }
}

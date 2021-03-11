package com.unizar.game;

import java.util.List;

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
}

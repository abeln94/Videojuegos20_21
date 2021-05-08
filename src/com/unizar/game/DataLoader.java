package com.unizar.game;

import com.unizar.game.elements.Element;
import com.unizar.game.elements.NPC;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DataLoader {

    public static final String FILE_NPCS = "npcs.txt";

    public static Set<Element> loadElements(String path, Game game) throws IOException, NoSuchFieldException, IllegalAccessException {
        final List<String> lines = Files.readAllLines(Paths.get(path, FILE_NPCS));

        Map<String, Element> elements = new HashMap<>();
        Element lastElement = null;
        for (String line : lines) {
            line = line.replaceAll("//.*", "").replaceAll("#.*", "").trim();
            if (line.isEmpty()) continue;

            if (line.matches("\\[.*\\]")) {
                lastElement = new NPC() {
                };
                elements.put(line.substring(1, line.length() - 1), lastElement);
            } else {
                final String[] split = line.split("=", 2);
                String property = split[0];
                String value = split[1];

                setValue(lastElement, property, value);
            }
        }

        return new HashSet<>(elements.values());
    }

    public static void saveElements(String path, Set<Element> elements) throws IOException {
        File folder = new File(path);
        folder.mkdirs();
        File output = new File(folder, FILE_NPCS);
        output.createNewFile();
        PrintWriter writer = new PrintWriter(output);

        for (Element npc : elements) {
            if (npc instanceof NPC) {
                writer.println("[" + npc.getClass().getSimpleName() + "]");
                writer.println("name=" + npc.name);
                writer.println("weight=" + npc.weight);
                if (npc.getLocation() != null)
                    writer.println("location=" + npc.getLocation().getClass().getSimpleName());
                writer.println();
                writer.println();
            }
        }


        writer.close();
    }
}

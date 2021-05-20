package com.unizar.game;

import com.unizar.game.elements.Element;
import com.unizar.game.elements.Item;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;

import javax.swing.*;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Debug functions
 */
public class Debug {
    public static void teleportPlayer(Game game) {

        Stream<Option> locations = game.world.elements.stream()
                .filter(element -> element instanceof Location)
                .sorted(Comparator.comparing(a -> a.id))
                .flatMap(Debug::getOptions);
        Stream<Option> onNulls = game.world.elements.stream().
                filter(element -> !(element instanceof Location))
                .filter(element -> element.getLocation() == null)
                .flatMap(Debug::getOptions)
                .map(Option::p);
        Object[] options = Stream.concat(
                locations,
                Stream.concat(
                        Stream.of(new Option(null)),
                        onNulls)
        ).toArray();

        Option selection = (Option) JOptionPane.showInputDialog(
                null,
                "Teleport to:",
                "DEBUG",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null
        );
        if (selection != null) {
            game.getPlayer().moveTo(selection.element);
        }
    }

    private static Stream<Option> getOptions(Element element) {
        return Stream.concat(
                Stream.of(new Option(element)),
                element.elements.stream()
                        .flatMap(Debug::getOptions)
                        .map(Option::p)
        );
    }

    private static class Option {
        Element element;
        String padding = "";

        public Option(Element element) {
            this.element = element;
        }

        protected Option p() {
            this.padding = "        " + this.padding;
            return this;
        }

        @Override
        public String toString() {
            if (element == null) return "<null>";
            return padding + element.id + ": " + element + " (" +
                    (element instanceof NPC ? "NPC" :
                            element instanceof Item ? "ITEM" :
                                    element instanceof Location ? "LOCATION" :
                                            "UNKNOWN")
                    + ")";
        }
    }

}

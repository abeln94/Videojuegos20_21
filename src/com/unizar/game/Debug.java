package com.unizar.game;

import com.unizar.game.elements.Element;

import javax.swing.*;
import java.util.stream.Stream;

/**
 * Debug functions
 */
public class Debug {
    public static void teleportPlayer(Game game) {

        final Object[] options = game.world.elements.stream().filter(element -> element.getLocation() == null).flatMap(Debug::getOptions).toArray();

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
        return Stream.concat(Stream.of(new Option(element)),
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
            return padding + element.id + ": " + element;
        }
    }

}

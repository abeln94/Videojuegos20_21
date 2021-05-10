package com.unizar.game;

import com.unizar.game.elements.Element;
import com.unizar.game.elements.Location;
import com.unizar.game.elements.NPC;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Implements a searcher of elements
 */
public class ElementSearcher {

    static private class Node {
        Element element;
        String whereIs = null;
        String debug = "";

        public Node(Element element) {
            this.element = element;
            debug = element == null ? "NULL" : element.id;
        }

        public Node connect(Element element, String whereIs) {
            final Node newNode = new Node(element);
            newNode.whereIs = this.whereIs == null ? whereIs : this.whereIs;
            newNode.debug = (element == null ? "NULL" : element.id) + " <- " + this.debug;
            return newNode;
        }
    }

    public static String searchElement(Element from, Element to) {
        if (from == to) return "Eres tu!";

        // variables
        Set<Element> visited = new HashSet<>();
        Queue<Node> toCheck = new LinkedList<>();

        // initialize
        toCheck.add(new Node(from));

        // iterate
        while (!toCheck.isEmpty()) {
            Node node = toCheck.remove();
            Element check = node.element;
            String whereIs = node.whereIs;

            // skip invalid
            if (check == null) continue;

            // skip visited
            if (visited.contains(check)) continue;
            visited.add(check);

            // debug
            System.out.println(node.debug);

            // check found
            if (check == to) {
                return (whereIs == null ? "Estás en " + to : to + " " + whereIs) + ".";
            }

            // check inventory
            check.elements.forEach(e -> toCheck.add(node.connect(e,
                    check == from ? "lo tienes en el inventario"
                            : "está ahí"
            )));

            // check wearables
            if (check instanceof NPC) {
                ((NPC) check).wearables.forEach(e -> toCheck.add(node.connect(e,
                        whereIs == null ? "lo llevas puesto" : whereIs
                )));
            }

            // check parent
            toCheck.add(node.connect(check.getLocation(), whereIs)); // only possible null

            // check exits
            if (check instanceof Location) {
                ((Location) check).exits.forEach((dir, p) -> {
                            toCheck.add(node.connect(p.first,
                                    whereIs == null ? "está hacia " + dir.description : whereIs
                            ));
                            toCheck.add(node.connect(p.second, "está ahí"));
                        }
                );
            }

        }

        return "No se ha podido encontrar " + to + ".";
    }
}

package com.unizar.game;

import com.unizar.game.elements.*;

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
        String debug;
        String type;

        public Node(Element element) {
            this.element = element;
            debug = element == null ? "NULL" : element.id;
        }

        public Node connect(Element element, String whereIs, String type) {
            final Node newNode = new Node(element);
            newNode.whereIs = this.whereIs == null ? whereIs : this.whereIs;
            newNode.debug = (element == null ? "NULL" : element.id) + " <- " + this.debug;
            newNode.type = type;
            return newNode;
        }
    }

    public static String searchElement(Element from, Element to) {
        if (from == to) return "Eres tu!";

        // variables
        Set<Element> visited = new HashSet<>();
        Queue<Node> toCheck = new LinkedList<>();
        String graph = "";

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

            generateConnections(node, from, toCheck);

        }
        System.out.println(graph);
        return "No se ha podido encontrar " + to + ".";
    }

    private static void generateConnections(Node node, Element from, Queue<Node> toCheck) {
        Element check = node.element;
        String whereIs = node.whereIs;


        // check inventory
        check.elements.forEach(e -> toCheck.add(node.connect(e,
                check == from ? "lo tienes en el inventario"
                        : "está ahí",
                "CHILD"
        )));

        // check wearables
        if (check instanceof NPC) {
            ((NPC) check).wearables.forEach(e -> toCheck.add(node.connect(e,
                    whereIs == null ? "lo llevas puesto" : whereIs,
                    "WEAR"
            )));
        }

        // check parent
        toCheck.add(node.connect(check.getLocation(), whereIs, null)); // only possible null

        // check exits
        if (check instanceof Location) {
            ((Location) check).exits.forEach((dir, p) -> {
                        toCheck.add(node.connect(p.first,
                                whereIs == null ? "está hacia " + dir.description : whereIs,
                                "GO:" + dir.name()
                        ));
                        toCheck.add(node.connect(p.second, "está ahí", "AT:" + dir.name()));
                    }
            );
        }
    }

    public static String generateGraph(Set<Element> elements) {
        final StringBuilder graph = new StringBuilder("digraph G {\n");


        elements.forEach(element -> {

            // node types
            graph.append(element.id + " [shape=" + (
                    element instanceof Player ? "doublecircle"
                            : element instanceof NPC ? "circle"
                            : element instanceof Item ? "diamond"
                            : element instanceof Location ? "box"
                            : "point"
            ) + "];\n");

            // node connections
            final LinkedList<Node> connections = new LinkedList<>();
            generateConnections(new Node(element), null, connections);
            connections.stream()
                    .filter(n -> n.element != null)
                    .filter(n -> n.type != null)
                    .forEach(connection ->
                            graph.append(line(element, connection.element, connection.type)).append("\n")
                    );
        });

        // tp connections
        elements.stream().filter(e -> e instanceof NPC).forEach(npc -> {
            final Element destination = ((NPC) npc).moveNPCsTo;
            if (destination != null) {
                graph.append(line(npc, destination, "TP")).append("\n");
            }
        });

        // hidden connections
        elements.forEach(e -> e.hiddenElements.forEach((a, h) -> {
            graph.append(line(e, h, a.name()));
        }));


        return graph.append("}").toString();
    }

    private static String line(Element from, Element to, String label) {
        return from.id + " -> " + to.id + " [label=\"" + label + "\"];";
    }
}

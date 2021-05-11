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
        System.out.println(graph);
        return "No se ha podido encontrar " + to + ".";
    }

    public static String generateGraph(Set<Element> elements) {
        final StringBuilder graph = new StringBuilder("digraph G {\n");

        final int[] unique = {0};

        elements.forEach(element -> {

            // node types
            graph.append(node(element,
                    element instanceof Player ? "doublecircle"
                            : element instanceof NPC ? "oval"
                            : element instanceof Item ? "diamond"
                            : element instanceof Location ? "box"
                            : "point"
            ));

            // inventory
            element.elements.forEach(child ->
                    graph.append(connection(element, child, "HAS", null)
                    ));

            // wearables
            if (element instanceof NPC) {
                ((NPC) element).wearables.forEach(wearable ->
                        graph.append(connection(element, wearable, "WEAR", null)
                        ));
            }

            // exits
            if (element instanceof Location) {
                ((Location) element).exits.forEach((dir, p) -> {
                            if (p.second == null) {
                                // direct
                                graph.append(connection(element, p.first, "GO:" + dir.name(), "bold"));
                            } else {
                                // through
                                String uniqueNode = "u_" + (unique[0]++);
                                graph.append(node(uniqueNode, "point"));
                                graph.append(connection(element, uniqueNode, "GO:" + dir.name(), "bold"));
                                graph.append(connection(uniqueNode, p.first, "TO", "bold"));
                                graph.append(connection(uniqueNode, p.second, "THROUGH", null));
                            }
                        }
                );
            }

            // TP connections
            if (element instanceof NPC) {
                Element destination = ((NPC) element).moveNPCsTo;
                if (destination != null) {
                    graph.append(connection(element, destination, "TP", "dotted"));
                }
            }

            // hidden connections
            element.hiddenElements.forEach((a, h) -> {
                graph.append(connection(element, h, a.name(), "dashed"));
            });

            // giveable items
            if (element instanceof NPC) {
                ((NPC) element).giveItems.forEach(giveable ->
                        graph.append(connection(element, giveable, "GIVE", "dashed"))
                );
            }
        });

        return graph.append("}").toString();
    }

    private static String node(Object node, String shape) {
        return (node instanceof Element ? ((Element) node).id : node.toString())
                + " ["
                + (shape != null ? "shape=" + shape + " " : "")
                + "];\n";
    }

    private static String connection(Object from, Object to, String label, String style) {
        return (from instanceof Element ? ((Element) from).id : from.toString())
                + " -> "
                + (to instanceof Element ? ((Element) to).id : to.toString())
                + " ["
                + (label != null ? "label=\"" + label + "\" " : "")
                + (style != null ? "style=" + style + " " : "")
                + "];\n";
    }
}

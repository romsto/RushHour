package fr.rstr.rushhour.utils.saves;

import fr.rstr.rushhour.Graph;
import fr.rstr.rushhour.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Save {

    /**
     * Save Graph to file
     *
     * @param graph        Graph to save
     * @param configNumber corresponding config
     * @throws IOException IO error
     */
    public static void saveGraph(Graph graph, int configNumber) throws IOException {
        Node node = graph.getInitial();
        String chain = convertNode(node, 0);

        String root = "saves/save_" + (configNumber < 10 ? "0" + configNumber : configNumber) + ".rushhour";

        File file = new File(root);
        file.createNewFile();
        FileWriter printer = new FileWriter(root);
        printer.write(chain);
        printer.close();
    }

    /**
     * Convert nodes in string
     *
     * @param node to convert
     * @return String
     */
    private static String convertNode(Node node, int i) {
        StringBuilder builder = new StringBuilder(String.valueOf(i)).append("@").append(node.convert()).append("@");
        String prefix = "";
        for (Node next : node.nodes) {
            builder.append(prefix);
            prefix = "¤" + i + "¤";
            builder.append(convertNode(next, i + 1));
        }

        return builder.toString();
    }

    /**
     * Load a graph if already exists.
     *
     * @param configNumber corresponding config
     * @return Graph
     * @throws IOException does not exist
     */
    public static Graph loadGraph(int configNumber) throws IOException {
        Path path = Path.of("saves/save_" + (configNumber < 10 ? "0" + configNumber : configNumber) + ".rushhour");

        if (!Files.exists(path))
            throw new IOException("This configuration hasn't already computed.");

        String graphString = Files.readString(path);

        String[] split = graphString.split("@", 3); // i@Node@...i¤...i¤...
        Node nodeInitial = new Node(split[1]); // Creation of Genesis Node

        Graph graph = new Graph(nodeInitial);

        if (split.length <= 2 || split[2] == null || split[2].isEmpty() || split[2].isBlank())
            return graph; // Node doesn't have some following node: we stop

        // Else we add each following nodes
        String[] followings = split[2].split("¤" + split[0] + "¤");
        for (String next : followings)
            addNode(nodeInitial, next);

        return graph;
    }

    /**
     * Add successively nodes by reading the string
     *
     * @param node      actual node
     * @param prochains string
     */
    private static void addNode(Node node, String prochains) {
        String[] split = prochains.split("@", 3); // i@Node@...i¤...i¤...

        Node newNode = new Node(split[1]); // Node creation

        node.nodes.add(newNode); // Add it to the actual node

        if (split.length <= 2 || split[2] == null || split[2].isEmpty() || split[2].isBlank())
            return; // Node doesn't have some following node: we stop

        // Else we add each following nodes
        String[] followings = split[2].split("¤" + split[0] + "¤");
        for (String next : followings)
            addNode(newNode, next);
    }
}

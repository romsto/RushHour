package fr.rstr.rushhour;

import java.util.ArrayList;
import java.util.List;

public class Node {

    // Children
    public final List<Node> nodes;
    private final String changement;

    /**
     * Create a node
     *
     * @param head     grid on top of the node
     * @param lastMove last move performed to get the grid
     */
    public Node(Grid head, Move lastMove) {
        this.changement = new Changement(head, lastMove).convertir();
        this.nodes = new ArrayList<>();
    }

    /**
     * fr.rstr.rushhour.Node from string
     *
     * @param node (conversion)
     */
    public Node(String node) {
        this.changement = node;
        this.nodes = new ArrayList<>();
    }

    /**
     * Get the head of the current node
     *
     * @return grid on top
     */
    public String getHead() {
        return changement.split("/")[0];
    }

    /**
     * Get the node children
     *
     * @return List of nodes
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * @return Last move
     */
    public String getLastMove() {
        return changement.split("/")[1];
    }

    /**
     * Check if a grid exists in the graph, if yes it returns the node
     *
     * @param grid grid to check
     * @return fr.rstr.rushhour.Node
     */
    public boolean exists(String grid) {
        if (getHead().equals(grid))
            return true;

        if (nodes.isEmpty())
            return false;

        for (Node node : nodes) {
            if (node.exists(grid))
                return true;
        }

        return false;
    }

    /**
     * Get all finishing paths
     *
     * @return List of paths
     */
    public List<List<Changement>> listAllPaths() {
        Grid grid = new Grid(getHead());
        if (grid.isFinished()) {
            List<Changement> list = new ArrayList<>();
            list.add(new Changement(changement));
            List<List<Changement>> listOfList = new ArrayList<>();
            listOfList.add(list);
            return listOfList;
        }

        if (nodes.isEmpty())
            return null;

        List<List<Changement>> nextPath = new ArrayList<>();

        for (Node node : nodes) {
            List<List<Changement>> existPath = node.listAllPaths();
            if (existPath != null) {
                for (List<Changement> grilles : existPath) {
                    grilles.add(0, new Changement(changement));
                    nextPath.add(grilles);
                }
            }
        }

        return nextPath;
    }

    /**
     * Count number of nodes
     *
     * @return number of node
     */
    public int computeNodes() {
        if (nodes.isEmpty())
            return 1;

        int somme = 1;
        for (Node i : nodes) {
            somme += i.computeNodes();
        }
        return somme;
    }

    /**
     * Convert node to string
     *
     * @return fr.rstr.rushhour.Node
     */
    public String convert() {
        return changement;
    }

}

package fr.rstr.rushhour;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Graph {

    private final Node initial;

    /**
     * Create fr.rstr.rushhour.Graph from an initial node
     *
     * @param grid fr.rstr.rushhour.Grid on top
     */
    public Graph(Grid grid) {
        this.initial = new Node(grid, null);
    }

    /**
     * Create fr.rstr.rushhour.Graph from existing node
     *
     * @param node existing
     */
    public Graph(Node node) {
        this.initial = node;
    }

    /**
     * Get the initial node
     *
     * @return fr.rstr.rushhour.Node initial
     */
    public Node getInitial() {
        return initial;
    }


    /**
     * Create the graph with all possible combination
     */
    public void build() {
        Queue<Node> toCompute = new LinkedList<>(); // Creating a queue with all following nodes
        toCompute.add(initial); // We begin at the initial node

        while (!toCompute.isEmpty()) {
            Node node = toCompute.poll(); // We pop the first node
            Grid grid = new Grid(node.getHead());

            // First case: we reach the final case, so we leave the loop
            if (grid.isFinished())
                continue;

            List<Changement> changements = grid.nextGrids(new Move(node.getLastMove()));

            // Second case : we compute all possible changes
            for (Changement changement : changements) {
                boolean lastNodeExists = initial.exists(changement.grid.convert());

                // Checking the combination not already exist
                if (!lastNodeExists) {
                    // We create the next node
                    Node newNode = new Node(changement.grid, changement.lastMove);
                    node.nodes.add(newNode); // And add it to the queue

                    if (new Grid(newNode.getHead()).isFinished())
                        continue;

                    toCompute.add(newNode);
                }
            }
        }
    }
}

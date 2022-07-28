package fr.rstr.rushhour;

import fr.rstr.rushhour.utils.csv.CSVParser;
import fr.rstr.rushhour.utils.gui.GUI;
import fr.rstr.rushhour.utils.saves.Save;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main extends Application {

    public static final int DIMENSION = 6;
    public static GUI GUI;

    /**
     * Get the generated graph from a config
     *
     * @param configuration number of config
     * @param built         generate the graph or not
     * @return fr.rstr.rushhour.Graph
     * @throws IOException unknown config
     */
    public static Graph fetchConfigurationGraph(int configuration, boolean built) throws IOException {
        if (!built)
            return new Graph(new CSVParser(configuration).convert());

        Graph graph;
        try {
            graph = Save.loadGraph(configuration);
        } catch (IOException e) {
            Grid grid = new CSVParser(configuration).convert();
            graph = new Graph(grid);
            graph.build();
        }
        return graph;
    }

    /**
     * Get the shortestPath from a graph
     *
     * @param graph to get
     * @return Path
     */
    public static List<Changement> shortestPath(Graph graph) {
        List<List<Changement>> grids = graph.getInitial().listAllPaths();
        int min = grids.get(0).size();
        List<Changement> shortest = grids.get(0);
        for (List<Changement> list : grids) {
            if (list.size() < min) {
                min = list.size();
                shortest = list;
            }
        }
        return shortest;
    }

    /**
     * Display all steps
     *
     * @param gui  to show
     * @param path path
     */
    public static void showSuccessively(GUI gui, List<Changement> path) {
        Queue<Changement> queue = new LinkedList<>(path);
        queue.poll();
        while (!queue.isEmpty()) {
            Changement changement = queue.poll();
            gui.moveVehicles(changement.grid, true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Solves all configs and save them
     *
     * @throws IOException if a config is missing
     */
    public static void solvesAllConfigurations() throws IOException {
        for (int i = 1; i <= 40; i++) {
            System.out.println("=== Configuration " + i + " ===");
            long start = System.currentTimeMillis();
            Graph graph = fetchConfigurationGraph(i, true);
            System.out.println("Building in " + (System.currentTimeMillis() - start) + "ms");
            System.out.println("Saving...");
            start = System.currentTimeMillis();
            Save.saveGraph(graph, i);
            System.out.println("Save in " + (System.currentTimeMillis() - start) + "ms");
            start = System.currentTimeMillis();
            List<List<Changement>> grids = graph.getInitial().listAllPaths();
            System.out.println("Paths in " + (System.currentTimeMillis() - start) + "ms");
            System.out.println("Possibilities : " + grids.size());
            int min = grids.get(0).size();
            for (List<Changement> list : grids) {
                if (list.size() < min)
                    min = list.size();
            }
            System.out.println("Shortest : " + min + " moves.");
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage st) throws Exception {
        GUI = new GUI(st);

        Graph graph = fetchConfigurationGraph(1, false);
        GUI.loadGraph(graph);
    }

}

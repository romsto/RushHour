package fr.rstr.rushhour.utils.gui;

import fr.rstr.rushhour.Changement;
import fr.rstr.rushhour.Graph;
import fr.rstr.rushhour.Grid;
import fr.rstr.rushhour.Main;
import fr.rstr.rushhour.utils.IVehicle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GUI {

    public final StringProperty labelProperty = new SimpleStringProperty();
    private final List<Shape> vehicle = new ArrayList<>();
    private final Pane pane;
    public Grid actualGrid;
    public int moves = 0;
    private Graph graph;

    public GUI(Stage stage) {
        BorderPane bp = new BorderPane();

        pane = new Pane();

        InputStream input = getClass().getResourceAsStream("../images/background.png");
        assert input != null;
        Image backgroundImage = new Image(input);
        ImageView background = new ImageView(backgroundImage);
        pane.getChildren().add(background);

        bp.setCenter(pane);
        bp.setRight(createControlPane());

        Scene sc = new Scene(bp, 550, backgroundImage.getHeight());

        stage.setTitle("Rush Hour");
        stage.setScene(sc);
        stage.show();
    }

    /**
     * Load a graph on the GUI
     *
     * @param graph to load
     */
    public void loadGraph(Graph graph) {
        labelProperty.set("");
        moves = 0;
        this.graph = graph;
        this.pane.getChildren().removeIf(node -> node instanceof Shape);
        this.vehicle.clear();
        placeVehicles(new Grid(graph.getInitial().getHead()));
    }

    /**
     * Add vehicles to the grid
     *
     * @param grid to add
     */
    public void placeVehicles(Grid grid) {
        this.actualGrid = grid;
        for (IVehicle vehicle : grid.vehicles) {
            Shape shape = new Shape(vehicle.getVehicleType(), vehicle.getRgb(), vehicle.getOrientation(), this);
            shape.setPosition(vehicle.getRow(), vehicle.getLine());
            this.pane.getChildren().add(shape);
            this.vehicle.add(shape);
        }
        this.vehicle.forEach(Shape::computeNextPositions);
    }

    /**
     * Move vehicles
     *
     * @param grid      List of all vehicles
     * @param animation Whether play an animation or not
     */
    public void moveVehicles(Grid grid, boolean animation) {
        moves++;
        this.actualGrid = grid;
        for (IVehicle vehicle : grid.vehicles) {
            for (Shape shape : this.vehicle) {
                if (shape.getColor() == vehicle.getRgb()) {
                    if (vehicle.getRow() != shape.getRow() || vehicle.getLine() != shape.getLine()) {
                        if (animation)
                            shape.move(vehicle.getRow(), vehicle.getLine());
                        else
                            shape.setPosition(vehicle.getRow(), vehicle.getLine());
                    }
                    break;
                }
            }
        }
        this.vehicle.forEach(Shape::computeNextPositions);
    }

    /**
     * Create the control Pane
     *
     * @return Pane control
     */
    private Pane createControlPane() {
        Pane ap = new Pane();

        Label label = new Label("");
        label.setPrefWidth(200);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setLayoutX(0);
        label.setLayoutY(20);
        label.textProperty().bind(labelProperty);
        ap.getChildren().add(label);

        TextField num = new TextField();
        num.setPrefWidth(100);
        num.setPromptText("Configuration number");
        num.setOnAction(event -> {
            try {
                int config = Integer.parseInt(num.getText());
                loadGraph(Main.fetchConfigurationGraph(config, false));
            } catch (Exception e) {
                num.setText("");
            }
        });
        num.setLayoutX(51);
        num.setLayoutY(165);
        ap.getChildren().add(num);

        Button load = new Button("Load");
        load.setOnAction(event -> {
            try {
                int config = Integer.parseInt(num.getText());
                loadGraph(Main.fetchConfigurationGraph(config, false));
            } catch (Exception e) {
                num.setText("");
            }
        });
        load.setPrefWidth(85);
        load.setPrefHeight(17);
        load.setLayoutX(58);
        load.setLayoutY(203);
        ap.getChildren().add(load);

        Button solve = new Button("Solve");
        solve.setOnAction(event -> {
            try {
                int config = Integer.parseInt(num.getText());
                loadGraph(Main.fetchConfigurationGraph(config, true));

                List<Changement> path = Main.shortestPath(graph);
                labelProperty.set("Possible in " + (path.size() - 1) + " moves.");
                new Thread(() -> Main.showSuccessively(this, path)).start();
            } catch (Exception e) {
                num.setText("");
            }
        });
        solve.setPrefWidth(85);
        solve.setPrefHeight(17);
        solve.setLayoutX(58);
        solve.setLayoutY(241);
        ap.getChildren().add(solve);

        return ap;
    }

}

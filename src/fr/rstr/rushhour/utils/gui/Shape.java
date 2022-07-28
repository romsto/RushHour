package fr.rstr.rushhour.utils.gui;

import fr.rstr.rushhour.Grid;
import fr.rstr.rushhour.Vehicle;
import fr.rstr.rushhour.utils.ChangeablePair;
import fr.rstr.rushhour.utils.IVehicle;
import fr.rstr.rushhour.utils.Orientation;
import fr.rstr.rushhour.utils.TypeVehicle;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Shape extends Group {
    private static final int a = 44; // a is a case pixel size
    private final Orientation orientation;
    private final GUI gui;
    private final List<ChangeablePair<ChangeablePair<Integer, Integer>, Grid>> nextCases = new ArrayList<>();
    public int color;
    private int line = 0, rown = 0;

    /**
     * JavaFX object
     *
     * @param type        of vehicle
     * @param color       of vehicle
     * @param orientation of vehicle
     * @param gui         game pane
     */
    public Shape(TypeVehicle type, int color, Orientation orientation, GUI gui) {
        this.gui = gui;
        int length = type.getSize() * a;
        this.orientation = orientation;
        this.color = color;

        Rectangle r = new Rectangle(length - 8, a - 8, Color.web(String.format("#%06X", (0xFFFFFF & color))));
        InputStream input = getClass().getResourceAsStream("../images/" + type.toString().toLowerCase() + ".png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);

        getChildren().add(r);
        getChildren().add(imageView);

        Rotate rotation = new Rotate();
        rotation.setAngle(orientation.getRotation() / Math.PI * 180);
        rotation.setPivotX(a / 2f);
        rotation.setPivotY(a / 2f);
        getTransforms().add(rotation);

        r.setLayoutX(3);
        r.setLayoutY(3);

        setLayoutX(rown * a + 45);
        setLayoutY(line * a + 43);

        // Movement manager
        final ChangeablePair<Double, Double> position = ChangeablePair.of(0d, 0d);

        setOnMousePressed(event -> {
            position.setFirst(getLayoutX() - event.getSceneX());
            position.setSecond(getLayoutY() - event.getSceneY());
            setCursor(Cursor.MOVE);
            setOpacity(0.6);
        });
        setOnMouseReleased(event -> {
            setCursor(Cursor.HAND);
            setOpacity(1);
            if (!canReachCase(getLayoutX() + 5, getLayoutY() + 5)) {
                setLayoutX(rown * a + 45);
                setLayoutY(line * a + 43);
            }
        });
        setOnMouseDragged(event -> {
            if (orientation == Orientation.HORIZONTAL)
                setLayoutX(event.getSceneX() + position.getFirst());
            else
                setLayoutY(event.getSceneY() + position.getSecond());
        });
        setOnMouseEntered(event -> setCursor(Cursor.HAND));
    }

    /**
     * Get vehicle row
     *
     * @return row
     */
    public int getRow() {
        return rown;
    }

    /**
     * Get vehicle line
     *
     * @return line
     */
    public int getLine() {
        return line;
    }

    /**
     * Get RGB color of vehicle
     *
     * @return RGB int
     */
    public int getColor() {
        return color;
    }

    /**
     * Set vehicle position without transition
     *
     * @param row  new row
     * @param line new line
     */
    public void setPosition(int row, int line) {
        this.rown = row;
        this.line = line;
        setLayoutX(row * a + 45);
        setLayoutY(line * a + 43);
    }

    /**
     * Move vehicle with animation
     *
     * @param row  new row
     * @param line new line
     */
    public void move(int row, int line) {
        this.rown = row;
        this.line = line;
        TranslateTransition translateTransition = new TranslateTransition();
        if (orientation == Orientation.HORIZONTAL)
            translateTransition.setToX(row * a + 45 - getLayoutX());
        else
            translateTransition.setToY(line * a + 43 - getLayoutY());
        translateTransition.setNode(this);
        translateTransition.setDuration(Duration.seconds(1));
        translateTransition.play();
    }

    /**
     * Compute possible movements
     */
    public void computeNextPositions() {
        nextCases.clear();

        Grid grid = gui.actualGrid;
        Vehicle vehicle = null;
        for (IVehicle ivehicule : grid.vehicles) {
            if (ivehicule.getRgb() == color) {
                vehicle = (Vehicle) ivehicule;
                break;
            }
        }

        assert vehicle != null;
        Grid nextGrid;
        switch (orientation) {
            case VERTICAL -> {
                nextGrid = vehicle.move(true);
                if (nextGrid != null)
                    nextCases.add(ChangeablePair.of(ChangeablePair.of(rown, line + 1), nextGrid));
                nextGrid = vehicle.move(false);
                if (nextGrid != null)
                    nextCases.add(ChangeablePair.of(ChangeablePair.of(rown, line - 1), nextGrid));
            }
            case HORIZONTAL -> {
                nextGrid = vehicle.move(true);
                if (nextGrid != null)
                    nextCases.add(ChangeablePair.of(ChangeablePair.of(rown + 1, line), nextGrid));
                nextGrid = vehicle.move(false);
                if (nextGrid != null)
                    nextCases.add(ChangeablePair.of(ChangeablePair.of(rown - 1, line), nextGrid));
            }
        }
    }

    /**
     * Know if the vehicle can reach the case in a movement
     *
     * @param x (position pixel)
     * @param y (position pixel)
     * @return true if possible
     */
    private boolean canReachCase(double x, double y) {
        for (ChangeablePair<ChangeablePair<Integer, Integer>, Grid> next : nextCases) {
            int row = next.getFirst().getFirst() * a + 45;
            int lign = next.getFirst().getSecond() * a + 43;
            if (Math.abs(x - row) <= a && Math.abs(y - lign) <= a) {
                gui.moveVehicles(next.getSecond(), false);
                gui.labelProperty.set(gui.moves + " move" + (gui.moves > 1 ? "s !" : " !"));
                if (getColor() == 0xFF0000 && next.getFirst().getFirst() >= 4)
                    gui.labelProperty.set("Congrats ! " + gui.moves + " moves !");
                return true;
            }
        }
        return false;
    }
}

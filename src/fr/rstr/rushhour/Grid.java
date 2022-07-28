package fr.rstr.rushhour;

import fr.rstr.rushhour.utils.IVehicle;
import fr.rstr.rushhour.utils.Orientation;
import fr.rstr.rushhour.utils.TypeVehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all vehicle positions
 * To move a vehicle, you need to create a new instance
 * Assertions: there is a red car on the third line
 *
 * @see Vehicle#move(boolean)
 */
public class Grid {

    public final List<IVehicle> vehicles; // Vehicles list

    /**
     * Creating an empty instance, do not forget to add vehicles!
     */
    public Grid() {
        this.vehicles = new ArrayList<>();
    }

    /**
     * Convert from a string
     *
     * @param grille to convert
     */
    public Grid(String grille) {
        this.vehicles = new ArrayList<>();

        // Extract all vehicles
        String[] vehiclesToConvert = grille.split(";");

        // For each, let's create an instance
        for (String vehicle : vehiclesToConvert) {

            String[] vehicleAttributs = vehicle.split(",");

            int color;
            int row;
            int line;
            Orientation orientation;
            TypeVehicle typeVehicle;

            if (vehicleAttributs[0].equals("V")) {
                typeVehicle = TypeVehicle.CAR;
            } else {
                typeVehicle = TypeVehicle.TRUCK;
            }

            color = Integer.decode(vehicleAttributs[1]);

            if (vehicleAttributs[2].equals("V")) {
                orientation = Orientation.VERTICAL;
            } else {
                orientation = Orientation.HORIZONTAL;
            }

            line = Integer.parseInt(vehicleAttributs[3]);
            row = Integer.parseInt(vehicleAttributs[4]);

            vehicles.add(new Vehicle(row, line, color, typeVehicle, orientation, this));
        }
    }

    /**
     * Check if the red vehicle is in the end position
     *
     * @return true if red car finished
     */
    public boolean isFinished() {
        // Get the red car
        IVehicle vehicule = null;
        for (IVehicle v : vehicles) {
            if (v.getRgb() == 0xFF0000) {
                vehicule = v;
                break;
            }
        }

        if (vehicule == null) {
            return false;
        }

        // Check the vehicle is on the right position
        return vehicule.getRow() == Main.DIMENSION - 2;
    }

    /**
     * Check if the case is free or if there is a vehicle on it
     *
     * @param row  to check
     * @param line to check
     * @return false if occupied
     */
    public boolean isFree(int row, int line) {
        // Check we're on the grid
        if (row < 0 || row >= Main.DIMENSION || line < 0 || line >= Main.DIMENSION)
            return false;

        // Check all vehicles
        for (IVehicle vehicule : vehicles) {
            // From its orientation
            if (vehicule.getOrientation() == Orientation.HORIZONTAL) {
                if (vehicule.getRow() <= row && row <= vehicule.getRow() + vehicule.getVehicleType().getSize() - 1
                        && vehicule.getLine() == line)
                    return false;
            }

            if (vehicule.getOrientation() == Orientation.VERTICAL) {
                if (vehicule.getRow() == row && vehicule.getLine() <= line
                        && line <= vehicule.getLine() + vehicule.getVehicleType().getSize() - 1)
                    return false;
            }
        }

        return true;
    }

    /**
     * Get all possible combination, except the one made by the last move
     *
     * @param lastMove to get this grid
     * @return all possible combinations
     */
    public List<Changement> nextGrids(Move lastMove) {
        List<Changement> nextList = new ArrayList<>();
        for (IVehicle vehicle : vehicles) {

            // fr.rstr.rushhour.Vehicle not concerned by last move
            if (lastMove == null || !vehicle.equals(lastMove.getVehicle())) {

                // Creating all grids
                Grid forwardGrid = ((Vehicle) vehicle).move(true);
                Grid backwardGrid = ((Vehicle) vehicle).move(false);

                // Add all grids if possible
                if (forwardGrid != null)
                    nextList.add(new Changement(forwardGrid, new Move(vehicle, true)));
                if (backwardGrid != null)
                    nextList.add(new Changement(backwardGrid, new Move(vehicle, false)));
            }

            // fr.rstr.rushhour.Vehicle concerned by last move
            else {
                Grid movement = ((Vehicle) vehicle).move(lastMove.isMovingForward());
                if (movement != null)
                    nextList.add(new Changement(movement, new Move(vehicle, lastMove.isMovingForward())));
            }
        }

        return nextList;
    }

    /**
     * Check if a grid is similar
     *
     * @param g to be compared
     * @return true if similar
     */
    public boolean isSimilarTo(Grid g) {

        for (IVehicle v : vehicles) {
            for (IVehicle v2 : g.vehicles) {

                // We check all vehicles
                if (v2.getRgb() == v.getRgb()) {

                    if (v.getRow() != v2.getRow() || v.getLine() != v2.getLine())
                        return false;
                    break;
                }
            }
        }

        return true;
    }

    /**
     * Convert to string
     *
     * @return converted to v1;v2...
     */
    public String convert() {
        StringBuilder builder = new StringBuilder();
        for (IVehicle vehicle : vehicles)
            builder.append(((Vehicle) vehicle).convert()).append(";");
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}

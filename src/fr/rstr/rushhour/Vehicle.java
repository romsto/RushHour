package fr.rstr.rushhour;

import fr.rstr.rushhour.utils.IVehicle;
import fr.rstr.rushhour.utils.Orientation;
import fr.rstr.rushhour.utils.TypeVehicle;

public class Vehicle implements IVehicle {

    private final int row, line, color;
    private final TypeVehicle type;
    private final Orientation orientation;
    private final Grid grid;

    public Vehicle(int row, int line, int color, TypeVehicle type, Orientation orientation, Grid grid) {
        this.row = row;
        this.line = line;
        this.color = color;
        this.type = type;
        this.orientation = orientation;
        this.grid = grid;
    }

    /**
     * Create a vehicle from old configuration
     *
     * @param old  vehicle to copy.
     * @param grid associated grid.
     */
    public Vehicle(IVehicle old, Grid grid) {
        this.row = old.getRow();
        this.line = old.getLine();
        this.color = old.getRgb();
        this.type = old.getVehicleType();
        this.orientation = old.getOrientation();
        this.grid = grid;
    }

    /**
     * Get the linked grid
     *
     * @return fr.rstr.rushhour.Grid
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * @return vehicle type
     */
    @Override
    public TypeVehicle getVehicleType() {
        return type;
    }

    /**
     * @return line
     */
    @Override
    public int getLine() {
        return line;
    }

    /**
     * @return row
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * @return orientation
     */
    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * @return hexadecimal color
     */
    @Override
    public int getRgb() {
        return color;
    }

    /**
     * Compute next grid by moving the vehicle
     *
     * @param forward going forward or backward
     * @return new grid if possible, or null if not
     */
    public Grid move(boolean forward) {

        // 1. Check the movement is possible
        int nextRow, nextLine, checkRow, checkLine;

        switch (orientation) {
            case HORIZONTAL -> {
                nextLine = this.line;
                checkLine = this.line;
                if (forward) {
                    nextRow = this.row + 1;
                    checkRow = this.row + this.type.getSize();
                } else {
                    nextRow = this.row - 1;
                    checkRow = nextRow;
                }
            }
            case VERTICAL -> {
                nextRow = this.row;
                checkRow = this.row;
                if (forward) {
                    nextLine = this.line + 1;
                    checkLine = this.line + this.type.getSize();
                } else {
                    nextLine = this.line - 1;
                    checkLine = nextLine;
                }
            }
            default -> {
                nextRow = this.row;
                nextLine = this.line;
                checkRow = this.row;
                checkLine = this.line;
            }
        }

        // Case already occupied
        if (!this.grid.isFree(checkRow, checkLine))
            return null;

        // 2. Create a new grid
        Grid nextGrid = new Grid();
        for (IVehicle vehicle : grid.vehicles) {
            Vehicle newVehicle;
            if (vehicle.equals(this)) {
                newVehicle = new Vehicle(nextRow, nextLine, color, type, orientation, nextGrid);
            } else {
                newVehicle = new Vehicle(vehicle, nextGrid);
            }
            nextGrid.vehicles.add(newVehicle);
        }

        return nextGrid;
    }

    /**
     * Convert to string
     *
     * @return format "type,color,orientation,line,row" (ie : C,75B401,V,2,0)
     */
    public String convert() {

        String chain;
        String t;
        if (type == TypeVehicle.TRUCK) {
            t = "C";
        } else {
            t = "V";
        }
        String o;
        if (orientation == Orientation.VERTICAL) {
            o = "V";
        } else {
            o = "H";
        }

        chain = "" + t + "," + color + "," + o + "," + line + "," + row;
        return chain;
    }
}

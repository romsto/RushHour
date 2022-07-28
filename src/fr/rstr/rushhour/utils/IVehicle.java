package fr.rstr.rushhour.utils;

public interface IVehicle {

    /**
     * @return vehicle type.
     */
    TypeVehicle getVehicleType();

    /**
     * @return get line
     */
    int getLine();

    /**
     * @return get row
     */
    int getRow();

    /**
     * @return vehicle orientation
     */
    Orientation getOrientation();

    /**
     * @return hexadecimal color
     */
    int getRgb();
}

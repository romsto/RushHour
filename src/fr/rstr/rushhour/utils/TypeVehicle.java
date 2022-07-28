package fr.rstr.rushhour.utils;

public enum TypeVehicle {

    TRUCK(3),
    CAR(2);

    private final int fSize;

    TypeVehicle(int pSize) {

        fSize = pSize;
    }

    /**
     * @return case length
     */
    public int getSize() {

        return fSize;
    }
}
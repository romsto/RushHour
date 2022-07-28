package fr.rstr.rushhour.utils;

public enum Orientation {

    HORIZONTAL(0), VERTICAL(Math.PI / 2);

    private final double fRotation;

    Orientation(double pRotation) {
        fRotation = pRotation;
    }

    /**
     * @return corresponding angle (radian)
     */
    public double getRotation() {
        return fRotation;
    }
}

package fr.rstr.rushhour;

import fr.rstr.rushhour.utils.IVehicle;

/**
 * Link a changement to a vehicle
 */
public class Move {

    private final IVehicle vehicule;
    private final boolean moveForward;
    private String color = null;

    public Move(IVehicle vehicule, boolean moveForward) {
        this.vehicule = vehicule;
        this.moveForward = moveForward;
    }

    public Move(String deplacement) {
        String[] valeurs = deplacement.split(",");
        this.color = valeurs[0];
        this.moveForward = valeurs[1].equals("a");
        this.vehicule = null;
    }

    /**
     * Get the concerned vehicle
     *
     * @return moving vehicle
     */
    public IVehicle getVehicle() {
        return vehicule;
    }

    /**
     * What kind of movement is it?
     *
     * @return forward or not
     */
    public boolean isMovingForward() {
        return moveForward;
    }

    /**
     * Convert to string
     *
     * @return format: color,a or r
     */
    public String convert() {
        return Integer.toHexString(vehicule.getRgb()) + (moveForward ? ",a" : ",r");
    }

    @Override
    public String toString() {
        if (vehicule == null) return color + " " + moveForward;
        if (moveForward) {
            return vehicule.getVehicleType() + " " + vehicule.getRgb() + " avance";
        } else {
            return vehicule.getVehicleType() + " " + vehicule.getRgb() + " recule";
        }
    }
}
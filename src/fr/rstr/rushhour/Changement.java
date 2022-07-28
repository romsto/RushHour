package fr.rstr.rushhour;

public class Changement {

    public final Grid grid;
    public final Move lastMove;

    public Changement(Grid grid, Move lastMove) {
        this.grid = grid;
        this.lastMove = lastMove;
    }

    /**
     * Convert a string to changement
     *
     * @param changement in string
     */
    public Changement(String changement) {
        String[] split = changement.split("/");
        grid = new Grid(split[0]);
        if (split[1].contains("rien"))
            lastMove = null;
        else
            lastMove = new Move(split[1]);
    }

    /**
     * Convert to String
     *
     * @return fr.rstr.rushhour.Changement string
     */
    public String convertir() {
        return grid.convert() + "/" + (lastMove == null ? "rien,a" : lastMove.convert());
    }
}

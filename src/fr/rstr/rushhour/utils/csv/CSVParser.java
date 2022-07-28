package fr.rstr.rushhour.utils.csv;

import fr.rstr.rushhour.Grid;
import fr.rstr.rushhour.Vehicle;
import fr.rstr.rushhour.utils.Orientation;
import fr.rstr.rushhour.utils.TypeVehicle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Convert a RH config file into playable grid.
 * Format: Size;Orientation;Line;Row;Color
 */
public class CSVParser {

    private final List<String> lines;

    /**
     * Reader of a config file
     *
     * @param number config
     * @throws IOException if the file does not exist
     */
    public CSVParser(int number) throws IOException {

        Path path = Path.of("configs/config_" + (number < 10 ? "0" + number : number) + ".csv");

        if (!Files.exists(path))
            throw new FileNotFoundException("Impossible to find config " + number + "...");

        String file = Files.readString(path);

        this.lines = new ArrayList<>();

        String[] readLines = file.split("\n");

        lines.addAll(Arrays.asList(readLines).subList(1, readLines.length));
    }

    /**
     * Convert read lines to Grid
     *
     * @return Grid
     */
    public Grid convert() {
        Grid grid = new Grid();
        for (String line : lines) {
            String[] values = line.split(";");

            TypeVehicle typeVehicle = (values[0].equals("2") ? TypeVehicle.CAR : TypeVehicle.TRUCK);
            Orientation orientation = Orientation.valueOf(values[1]);
            int y = Integer.parseInt(values[2]);
            int x = Integer.parseInt(values[3]);
            int color = Integer.decode(values[4].substring(0, values[4].length() - 1));

            grid.vehicles.add(new Vehicle(x, y, color, typeVehicle, orientation, grid));
        }

        return grid;
    }
}

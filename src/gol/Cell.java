package gol;

import java.util.List;

// Immutable, which allows threading and other Cells to safely keep copies of other Cells
public class Cell {
    private final int state;
    private final List<Coordinate> neighbourCoords; // (storing coords instead of direct references to minimise risk of cell mutation and maximise thread safety)

    // Private for immutability (by preventing subclassing)
    private Cell(int state, List<Coordinate> neighbourCoords) {
        this.state = state;
        this.neighbourCoords = neighbourCoords;   // TODO Clone??
    }

    public static Cell newCell(int state, List<Coordinate> neighbourCoords) {
        return new Cell(state, neighbourCoords);
    }

    @Override
    public String toString() {
        return Integer.toString(this.state);
    }

    public int getState() {
        return state;
    }

    public List<Coordinate> getNeighbourCoords() {
        return this.neighbourCoords;
    }
}

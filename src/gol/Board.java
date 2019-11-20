package gol;

import java.util.*;

// Only have one of these per iteration (for immutably / thread safety)
// TODO - (remove 2d array of cells) revamp so that each Cell has a Coord and so that the internal list is one dimensional
// TODO (continued) - perhaps use a hashmap with key of Coordinate (then also modify hashCode and equals function for Coord)

public class Board {
    private List<List<Cell>> cells;
    private Renderer renderer;

    // Immutable class to maximise thread-safety
    // Instead use the public static factory to create new instances
    private Board(List<List<Cell>> cells, Renderer renderer) {
    // TODO - implement Clone for the cells list?
        this.cells = cells;
        this.renderer = renderer;
        this.renderer.initRenderer();
    }

    public static Board newBoard(int size, Renderer renderer) {
        return new Board(createStartCells(size), renderer);
    }

    public void display(int roundNumber) {
        this.renderer.render(cells, roundNumber);
    }

    public void end() {
        this.renderer.closeRenderer();
    }

    // Construct a Board which represents the next state in the Game of Life algorithm
    // TODO - implement Threading
    public Board update() {
        int size = this.cells.size();
        List<List<Cell>> newCells = new ArrayList<>(size);

        // For each cell in this.cells, evaluate the GOL conditions, and store a new cell in newCells (copying neighbourCoords, etc)
        for (List<Cell> rowOfCells : this.cells) {
            List<Cell> newRowOfCells = new ArrayList<>(size);

            for (Cell cell : rowOfCells) {
                newRowOfCells.add(Cell.newCell(getNewCellState(cell), cell.getNeighbourCoords()));
            }
            newCells.add(newRowOfCells);
        }

        return new Board(newCells, renderer);   // (renderer remains the same)
    }

    private int getNewCellState(Cell cell) {
        int numLiveNeighbours = getNumberOfLiveNeighbours(cell);
        int newState = 0;
        if (cell.getState() == 1) {
            if (numLiveNeighbours > 3) {
                // overpopulation
                newState = 0;
            } else if (numLiveNeighbours < 2) {
                // underpopulation
                newState = 0;
            } else {
                // lives on
                newState = 1;
            }
        } else {
            if (numLiveNeighbours == 3) {
                newState = 1;
            }
        }
        return newState;
    }

    private int getNumberOfLiveNeighbours(Cell cell) {
        int numLiveNeighbours = 0;
        for (Coordinate coord : cell.getNeighbourCoords()) {
            if (getCellAt(coord).getState() == 1) {
                numLiveNeighbours++;
            }
        }
        return numLiveNeighbours;
    }

    // Set all cells to have state == 0, besides central cell/s
    private static List<List<Cell>> createStartCells(int size) {
        List<List<Cell>> newCells = new ArrayList<>(size);

        Random random = new Random();

        // Creating a board of Cells of size (size x size)
        for (int row = 0; row < size; row++) {
            List<Cell> rowOfCells = new ArrayList<>(size);

            for (int col = 0; col < size; col++) {
                int state = 0;

                // Set all neighbours to null for now
                rowOfCells.add(col, Cell.newCell(random.nextInt(2), null));
            }
            newCells.add(row, rowOfCells);
        }

        // Note, this could also be done in the above inner for loop (but is abstracted for readability). This comes at
        // a slight memory (and slighter performance) cost as new Cells are being allocated in the following method.
        setNeighbourCoordinates(newCells);

        return newCells;
    }

    // Setting the list of neighbouring cell's coordinates for each cell.
    private static void setNeighbourCoordinates(List<List<Cell>> newCells) {
        int size = newCells.size();

        for (int row = 0; row < size; row++) {

            for (int col = 0; col < size; col++) {
                List<Coordinate> neighboursCoordList = null;

                // Infinite board implementation - stitching the top with bottom and left with right
                neighboursCoordList = Arrays.asList(
                    new Coordinate(col, boundaryCalculation(row - 1, size)),
                    new Coordinate(boundaryCalculation(col + 1, size), boundaryCalculation(row - 1, size)),
                    new Coordinate(boundaryCalculation(col + 1, size), row),
                    new Coordinate(boundaryCalculation(col + 1, size), boundaryCalculation(row + 1, size)),
                    new Coordinate(col, boundaryCalculation(row + 1, size)),
                    new Coordinate(boundaryCalculation(col - 1, size), boundaryCalculation(row + 1, size)),
                    new Coordinate(boundaryCalculation(col - 1, size), row),
                    new Coordinate(boundaryCalculation(col - 1, size), boundaryCalculation(row - 1, size))
                );

                // Updating the cell at (col, row) to have a list of neighbours
                newCells.get(row).set(col, Cell.newCell(newCells.get(row).get(col).getState(), neighboursCoordList));
            }
        }
    }

    private static int boundaryCalculation(int index, int size) {
        return (index + size) % size;
    }

    private Cell getCellAt(Coordinate coord) {
        return this.cells.get(coord.getY()).get(coord.getX());
    }

    // Returns the cells at the specified coords. If a coord is duplicated, it will be added more than once.
    private List<Cell> getCellsAt(List<Coordinate> coords) {
        List<Cell> newCells = new ArrayList<>(coords.size());
        for (Coordinate coord : coords) {
            newCells.add(getCellAt(coord));
        }
        return newCells;
    }
}

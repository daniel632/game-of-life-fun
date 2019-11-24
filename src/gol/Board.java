package gol;

import java.util.*;

// Only have one of these per iteration (for immutably / thread safety)
public class Board {
    private List<List<Cell>> cells;
    private Renderer renderer;
    private Rule rule;

    // Immutable class to maximise thread-safety
    // Instead use the public static factory to create new instances
    private Board(List<List<Cell>> cells, Renderer renderer, Rule rule) {
    // TODO - implement Clone for the cells list?
        this.cells = cells;
        this.renderer = renderer;
        this.rule = rule;
    }

    public static Board newBoard(int size, Renderer renderer, Rule rule) {
        return new Board(createStartCells(size), renderer, rule);
    }

    public void display(int roundNumber) {
        this.renderer.render(cells, roundNumber);
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

        return new Board(newCells, this.renderer, this.rule);
    }

    // TODO - cell should be able to know how many live neighbours it has - note this requires storing the Cell and not just Coordinate for each neighbour
    private State getNewCellState(Cell cell) {
        return this.rule.apply(cell, getNumberOfLiveNeighbours(cell));
    }

    private int getNumberOfLiveNeighbours(Cell cell) {
        int numLiveNeighbours = 0;
        for (Coordinate coord : cell.getNeighbourCoords()) {
            if (getCellAt(coord).getState() == State.ALIVE) {
                numLiveNeighbours++;
            }
        }
        return numLiveNeighbours;
    }

    // Set all cells to have state == 0, besides central cell/s
    private static List<List<Cell>> createStartCells(int size) {
        List<List<Cell>> newCells = new ArrayList<>(size);

        // Creating a board of Cells of size (size x size)
        for (int row = 0; row < size; row++) {
            List<Cell> rowOfCells = new ArrayList<>(size);

            for (int col = 0; col < size; col++) {
                int state = 0;

                // Set all neighbours to null for now
                rowOfCells.add(col, Cell.newCell(State.randomState(), null));
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

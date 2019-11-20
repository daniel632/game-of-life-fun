package gol;

public interface Rule {
    State apply(Cell cell, int numLiveNeighbours);
}

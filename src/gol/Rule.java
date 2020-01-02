package gol;

import java.util.List;

public interface Rule {
    State apply(Cell cell, List<Cell> neighbours);
    State getRandomState();
}

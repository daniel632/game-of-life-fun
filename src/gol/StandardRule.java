package gol;

import java.util.*;

// TODO find way to abstract this singleton structure for the different Rule implmentations??
public class StandardRule implements Rule {
    private static Rule singleton = null;
    private StandardRule() {}

    private static final Random RANDOM = new Random();
    private static final List<State> states = Arrays.asList(State.ALIVE, State.DEAD);

    public static Rule newRule() {
        if (singleton == null) {
            singleton = new StandardRule();
        }
        return singleton;
    }

    public State apply(Cell cell, List<Cell> neighbours) {
        State newState = State.DEAD;
        int numLiveNeighbours = numAlive(neighbours);
        if (cell.getState() == State.ALIVE) {
            if (numLiveNeighbours > 3) {
                // overpopulation
                newState = State.DEAD;
            } else if (numLiveNeighbours < 2) {
                // underpopulation
                newState = State.DEAD;
            } else {
                // lives on
                newState = State.ALIVE;
            }
        } else {
            if (numLiveNeighbours == 3) {
                newState = State.ALIVE;
            }
        }
        return newState;
    }

    public State getRandomState() {
        return states.get(RANDOM.nextInt(states.size()));
    }

    private int numAlive(List<Cell> cell) {
        return (int) cell.stream().filter(i -> i.getState().equals(State.ALIVE)).count();
    }
}

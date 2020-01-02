package gol;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ImmigrationRule implements Rule {
    private static Rule singleton = null;
    private ImmigrationRule() {}

    private static final Random RANDOM = new Random();
    private static final List<State> states = Arrays.asList(State.ALIVE, State.ALIVE2, State.DEAD);

    public static Rule newRule() {
        if (singleton == null) {
            singleton = new ImmigrationRule();
        }
        return singleton;
    }

    // Immigration variation as described here: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Variations
    public State apply(Cell cell, List<Cell> neighbours) {
        State newState = State.DEAD;
        int numLiveNeighbours = numAlive(neighbours);
        if (isAlive(cell)) {
            if (numLiveNeighbours > 3) {
                // overpopulation
                newState = State.DEAD;
            } else if (numLiveNeighbours < 2) {
                // underpopulation
                newState = State.DEAD;
            } else {
                // lives on
                newState = cell.getState();
            }
        } else {
            // Immigration condition
            if (numLiveNeighbours == 3) {
                newState = majorityNeighbourState(neighbours);
                if (newState.equals(State.DEAD)) {
                    throw new RuntimeException("Invalid immigration birth state!");
                }
            }
        }
        return newState;
    }

    public State getRandomState() {
        // increase probability of dead
        int rand = RANDOM.nextInt(states.size() + 2);
        if (rand > states.size() - 1) {
            rand = states.size() - 1;
        }
        return states.get(rand);
    }

    private boolean isAlive(Cell cell) {
        return cell.getState().equals(State.ALIVE) || cell.getState().equals(State.ALIVE2);
    }

    private int numAlive(List<Cell> cell) {
        return (int) cell.stream().filter(i -> isAlive(i)).count();
    }

    private State majorityNeighbourState(List<Cell> neighbours) {
        int alive1Count = 0;
        int alive2Count = 0;

        for (Cell cell : neighbours) {
            switch (cell.getState()) {
                case ALIVE:
                    alive1Count++;
                    break;
                case ALIVE2:
                    alive2Count++;
                    break;
                default:
                    // do nothing for DEAD cells
            }
        }

        if (alive1Count > alive2Count) {
            return State.ALIVE;
        } else if (alive2Count > alive1Count) {
            return State.ALIVE2;
        }
        // even number of 'alive' cells (or none at all)
        return State.DEAD;  // Sentinel
    }
}

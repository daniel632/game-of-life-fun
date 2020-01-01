package gol;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ImmigrationRule implements Rule {
    private static Rule singleton = null;
    private ImmigrationRule() {}

    private static final Random RANDOM = new Random();
    private static final List<State> states = Arrays.asList(State.DEAD, State.ALIVE, State.ALIVE2);

    public static Rule newRule() {
        if (singleton == null) {
            singleton = new ImmigrationRule();
        }
        return singleton;
    }

    // Immigration variation as described here: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Variations
    public State apply(Cell cell, int numLiveNeighbours) {
        State newState = State.DEAD;
        return newState;
    }

    public State getRandomState() {
        return states.get(RANDOM.nextInt(states.size()));
    }
}

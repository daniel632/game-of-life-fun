package gol;

public class ImmigrationRule implements Rule {
    private static Rule singleton = null;

    private ImmigrationRule() {}

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
}

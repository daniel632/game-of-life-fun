package gol;

// TODO find way to abstract this singleton structure for the different Rule implmentations??
public class StandardRule implements Rule {
    private static Rule singleton = null;

    private StandardRule() {}

    public static Rule newRule() {
        if (singleton == null) {
            singleton = new StandardRule();
        }
        return singleton;
    }

    public State apply(Cell cell, int numLiveNeighbours) {
        State newState = State.DEAD;
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
}

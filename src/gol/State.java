package gol;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum State {
    DEAD,
    ALIVE;

    private static final List<State> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static State randomState()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    @Override
    public String toString() {
        return this.name();
    }
}

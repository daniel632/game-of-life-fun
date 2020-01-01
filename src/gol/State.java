package gol;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum State {
    DEAD(Color.GRAY),
    ALIVE(Color.GREEN),
    ALIVE2(Color.BLUE);

    private static final List<State> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private Color color;

    State(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return this.name();
    }
}

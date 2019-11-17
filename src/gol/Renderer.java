package gol;

import java.util.List;

public interface Renderer {
    void initRenderer();
    void closeRenderer();
    void render(List<List<Cell>> cells, int roundNumber);
}

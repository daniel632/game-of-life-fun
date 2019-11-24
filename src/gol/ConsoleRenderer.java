package gol;

import java.util.List;

// TODO - perhaps implement some sort of getter / factory class as other renderers should also be singletons
public class ConsoleRenderer implements Renderer {
    // TODO - implement an enum for these?
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    private static ConsoleRenderer singleton = null;

    private ConsoleRenderer() {}

    public static Renderer getRenderer() {
        if (singleton == null) {
            singleton = new ConsoleRenderer();
        }
        return singleton;
    }

    @Override
    public void initRenderer(int size) {
    }

    @Override
    public void closeRenderer() {
    }

    @Override
    public void render(List<List<Cell>> cells, int roundNumber) {
        // (TODO - double check that this is an efficient way to handle strings?)

        System.out.print("\033[2J\033[;H");
        System.out.flush();

        String text = "Round: " + roundNumber + "\n\n";
        for (List<Cell> rowOfCells : cells) {
            String line = "";
            for (Cell cell : rowOfCells) {
                if (cell.getState() == State.ALIVE) {
                    line += ANSI_GREEN_BACKGROUND + "   " + ANSI_RESET;
                } else {
                    line += "   ";
                }
            }
            line += "\n";
            text += line;
        }
        text += "\n";
        System.out.print(text);
    }
}

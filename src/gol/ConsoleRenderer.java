package gol;

import java.util.List;

// TODO - perhaps implement some sort of getter / factory class as other renderers should also be singletons
public class ConsoleRenderer implements Renderer {
    private static ConsoleRenderer singleton = null;

    private ConsoleRenderer() {}

    public static Renderer getRenderer() {
        if (singleton == null) {
            singleton = new ConsoleRenderer();
        }
        return singleton;
    }

    @Override
    public void render(List<List<Cell>> cells) {
        // (TODO - double check that this is an efficient way to handle strings?)

        System.out.print("\033[2J\033[;H");
        System.out.flush();

        String text = "\n\n";
        for (List<Cell> rowOfCells : cells) {
            String line = "";
            for (Cell cell : rowOfCells) {
                line += cell.toString() + " ";
            }
            line += "\n";
            text += line;
        }
        text += "\n";
        System.out.print(text);
    }
}

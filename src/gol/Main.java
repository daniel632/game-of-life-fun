package gol;

import java.security.InvalidParameterException;

// NOTE: Because IntelliJ is weird with ANSI escape sequences, need to run this in a standard terminal
// I'm compiling the files then running:
// java -classpath /Users/{user}/Code/GameOfLife/out/production/GameOfLife gol.Main <board size> <num of rounds>
// <{'gui, 'console'}>

public class Main {
    private enum RendererType {
        GUI,
        CONSOLE;
    }

    private static class Attributes {
        public static final int NUM_ATTRS = 3;
        public static int boardSize = 0;
        public static int numRounds = 0;
        public static RendererType rendererType = RendererType.GUI;
    }

    private static final int MAX_BOARD_SIZE = 100;
    private static final int MIN_BOARD_SIZE = 3;
    private static final int MAX_NUM_ROUNDS = 1000;
    private static final int DELAY = 200;

    public static void main(String[] args) {
        parseArgs(args);

        Rule rule = ImmigrationRule.newRule();

        Renderer renderer = getRenderer(Attributes.rendererType);
        renderer.initRenderer(Attributes.boardSize);

        Board board = Board.newBoard(Attributes.boardSize, renderer, rule);
        board.display(0);

        for (int roundNum = 1; roundNum < Attributes.numRounds; roundNum++) {
            // TODO Perhaps use a scheduled executor instead?
            // TODO implement concurrency (will enable higher fps) - for update only (not display)
            golUtilities.sleep(DELAY);
            board = board.update();
            board.display(roundNum);
        }

        renderer.closeRenderer();
    }

    private static void parseArgs(String[] args) {
        if (args.length != Attributes.NUM_ATTRS) {
            throw new InvalidParameterException(
                    "Incorrect number of arguments to main. Expected: boardSize numRounds {gui, console}"
            );
        }
        Attributes.boardSize = Integer.parseInt(args[0]);
        Attributes.numRounds = Integer.parseInt(args[1]);

        if (Attributes.boardSize < MIN_BOARD_SIZE || Attributes.boardSize > MAX_BOARD_SIZE) {
            throw new InvalidParameterException("Board size must be greater than or equal to " + MIN_BOARD_SIZE +
                    " and less than or equal to " +
                    MAX_BOARD_SIZE);
        }
        if (Attributes.numRounds <= 0 || Attributes.numRounds > MAX_NUM_ROUNDS) {
            throw new InvalidParameterException("Number of rounds must be positive and less than or equal to " +
                    MAX_NUM_ROUNDS);
        }

        try {
            Attributes.rendererType = RendererType.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException(
                    "Incorrect renderer type: Must be one of {'gui', 'console'}"
            );
        }

        System.out.println("Size: " + Attributes.boardSize + "\nRounds: " + Attributes.numRounds);
    }

    private static Renderer getRenderer(RendererType rendererType) {
        switch (rendererType) {
            case GUI:
                return GUIRenderer.getRenderer();
            default:
                return ConsoleRenderer.getRenderer();
        }
    }
}

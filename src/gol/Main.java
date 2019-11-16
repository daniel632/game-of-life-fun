package gol;

import java.security.InvalidParameterException;

// NOTE: Because IntelliJ is weird with ANSI escape sequences, need to run this in a standard terminal
// I'm compiling the files then running:
// java -classpath /Users/{user}/Code/GameOfLife/out/production/GameOfLife gol.Main <board size> <num of rounds>

public class Main {
    private static class Attributes {
        public static final int NUM_ATTRS = 2;
        public static int boardSize = 0;
        public static int numRounds = 0;
    }
    private static final int MAX_BOARD_SIZE = 100;
    private static final int MIN_BOARD_SIZE = 3;
    private static final int MAX_NUM_ROUNDS = 100;

    public static void main(String[] args) {
        parseArgs(args);

        Board board = Board.newBoard(Attributes.boardSize, ConsoleRenderer.getRenderer());
        board.display();

        for (int roundNum = 1; roundNum < Attributes.numRounds; roundNum++) {
            // TODO Perhaps use a scheduled executor instead?
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
                Thread.currentThread().interrupt();
            }

            board = board.update();
            board.display();
        }

    }

    private static void parseArgs(String[] args) {
        if (args.length != Attributes.NUM_ATTRS) {
            throw new InvalidParameterException("Incorrect number of arguments to main. Expected: boardSize numRounds");
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

        System.out.println("Size: " + Attributes.boardSize + "\nRounds: " + Attributes.numRounds);
    }
}

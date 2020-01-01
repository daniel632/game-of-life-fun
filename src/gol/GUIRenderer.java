package gol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class GUIRenderer implements Renderer {
    private static GUIRenderer singleton = null;

    private GUIRenderer() {}

    public static Renderer getRenderer() {
        if (singleton == null) {
            singleton = new GUIRenderer();
        }
        return singleton;
    }

    private static final int CELL_PIXEL_LENGTH = 30;
    private static final int CELL_BORDER_PIXEL_LENGTH = CELL_PIXEL_LENGTH / 10;
    private static final int MILLI_PAUSE_BEFORE_EXIT = 3000;

    private int boardSideLength;
    private List<List<JPanel>> cellPanels;
    private JFrame frame;
    private JPanel infoPanel;
    private JTextPane roundNumberText;

    @Override
    public void initRenderer(int size) {
        this.boardSideLength = size;

        this.frame = new JFrame("Game of Life fun");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Cell Board:
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(this.boardSideLength, this.boardSideLength, this.CELL_BORDER_PIXEL_LENGTH,
                this.CELL_BORDER_PIXEL_LENGTH));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(this.CELL_BORDER_PIXEL_LENGTH,
                this.CELL_BORDER_PIXEL_LENGTH, this.CELL_BORDER_PIXEL_LENGTH, this.CELL_BORDER_PIXEL_LENGTH));

        this.cellPanels = new ArrayList<>(this.boardSideLength);
        for (int row = 0; row < this.boardSideLength; row++) {
            List<JPanel> rowCells = new ArrayList<>(this.boardSideLength);
            for (int col = 0; col < this.boardSideLength; col++) {
                JPanel cellPanel = new JPanel();
                cellPanel.setPreferredSize(new Dimension(this.CELL_PIXEL_LENGTH, this.CELL_PIXEL_LENGTH));
                cellPanel.setBackground(Color.GRAY);
                rowCells.add(cellPanel);
                boardPanel.add(cellPanel);
            }
            this.cellPanels.add(rowCells);
        }

        // TODO (somehow) ensure board/cells aspect ratio is 1

        // Game Info:
        this.infoPanel = new JPanel();
        this.roundNumberText = new JTextPane();
        this.roundNumberText.setText("Round: 0");
        this.roundNumberText.setEditable(false);
        this.infoPanel.add(roundNumberText);

        this.frame.getContentPane().add(BorderLayout.NORTH, infoPanel);
        this.frame.getContentPane().add(BorderLayout.CENTER, boardPanel);

        this.frame.pack();
        this.frame.setVisible(true);
    }

    @Override
    public void closeRenderer() {
        golUtilities.sleep(MILLI_PAUSE_BEFORE_EXIT);
        this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void render(List<List<Cell>> cells, int roundNumber) {
        if (cells.size() != this.boardSideLength) {
            throw new IllegalArgumentException("GUIRenderer.render: Invalid board size.");
        }

        this.roundNumberText.setText("Round: " + (roundNumber + 1));

        for (int row = 0; row < this.boardSideLength; row++) {
            for (int col = 0; col < this.boardSideLength; col++) {
                Cell cell = cells.get(row).get(col);
                this.cellPanels.get(row).get(col).setBackground(cell.getState().getColor());
            }
        }
    }
}

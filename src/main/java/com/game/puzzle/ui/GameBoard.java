package com.game.puzzle.ui;

import com.game.puzzle.logic.ImageTile;
import com.game.puzzle.logic.PuzzleGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * Callback with only mouseClick listener
 */
interface MouseClickListener extends MouseListener {
    @Override
    default void mousePressed(MouseEvent e) {}

    @Override
    default void mouseReleased(MouseEvent e) {}

    @Override
    default void mouseEntered(MouseEvent e) {}

    @Override
    default void mouseExited(MouseEvent e) {}
}

/**
 * Graphical swing representation of PuzzleGrid component
 */
public class GameBoard extends JPanel {
    private static final Dimension BOARD_SIZE = new Dimension(3, 3);
    private static final int PUZZLE_SPACING = 4;

    private PuzzleGrid grid = null;

    public GameBoard() {
        super();
        this.grid = GameBoard.getRandomPuzzleGrid();

        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.addMouseListener((MouseClickListener)(e -> {
            this.grid.slide(
                this.getPuzzleIndex(e)
            );
            this.repaint();
        }));
    }

    public PuzzleGrid getGrid() { return grid; }

    /**
     * Reorder puzzles and repaint component
     */
    public void shuffle() {
        this.grid.shuffle();
        this.repaint();
    }

    /**
     * @param e Mouse click / move event
     * @return  Slide index inside array
     */
    private Point getPuzzleIndex(MouseEvent e) {
        return new Point(
            e.getX() / this.getPuzzleSize().width,
            e.getY() / this.getPuzzleSize().height
        );
    }

    /**
     * @return  Single puzzle size relative to JPanel board size, its not image size
     */
    private Dimension getPuzzleSize() {
        final Dimension GRID_SIZE = this.grid.getPuzzles().getSize();
        return new Dimension(
                this.getSize().width / GRID_SIZE.width,
                this.getSize().height / GRID_SIZE.height
        );
    }

    /**
     * @return  Shuffled random tile with image
     */
    private static PuzzleGrid getRandomPuzzleGrid() {
        ImageTile tile = null;

        try {
            tile = new ImageTile(
                    Resources.getImage("cat.jpg"),
                    BOARD_SIZE,
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PuzzleGrid(tile);
    }

    /**
     * Draw puzzle grid
     *
     * @param g Graphics
     */
    @Override
    public void paint (Graphics g) {
        if (this.grid == null)
            return;

        super.paint(g);

        /**
         * Draw background
         */
        final Graphics2D imgContext = (Graphics2D) g.create();
        imgContext.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .15f));
        imgContext.drawImage(
            this.grid.getTile().getImage(),
                0, 0,
                this.getSize().width,
                this.getSize().height,
                null
        );
        imgContext.dispose();

        /**
         * Draw game grid
         */
        final Dimension puzzleSize = this.getPuzzleSize();
        final Graphics2D g2 = (Graphics2D) g;

        this.grid.getPuzzles().map((puzzle, x, y) -> {
            if (puzzle != null) {
                final BufferedImage img = (BufferedImage) puzzle.getImage();
                g2.drawImage(
                        img,
                        x * puzzleSize.width + GameBoard.PUZZLE_SPACING / 2,
                        y * puzzleSize.height + GameBoard.PUZZLE_SPACING / 2,
                        puzzleSize.width - GameBoard.PUZZLE_SPACING,
                        puzzleSize.height - GameBoard.PUZZLE_SPACING,
                        null
                );
            }
            return null;
        });
    }
}

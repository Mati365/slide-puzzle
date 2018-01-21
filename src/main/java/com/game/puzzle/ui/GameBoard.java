package com.game.puzzle.ui;

import com.game.puzzle.logic.ImageTile;
import com.game.puzzle.logic.PuzzleGrid;

import javax.swing.*;
import javax.validation.constraints.NotNull;
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
    @FunctionalInterface
    interface BoardListener {
        void puzzleOrdered();
    }

    /** Constants */
    private static final Dimension BOARD_SIZE = new Dimension(4, 4);
    private static final int PUZZLE_SPACING = 2;

    private PuzzleGrid grid = null;
    private BoardListener boardListener = null;
    private boolean disabled;

    public GameBoard(boolean disabled) {
        super();
        this.grid = GameBoard.getRandomPuzzleGrid();
        this.disabled = disabled;

        setLayout(null);
        setBackground(Color.WHITE);
        addMouseListener(
                (MouseClickListener)(this::triggerSlide)
        );
    }

    /**
     * Disable any changes in board
     *
     * @param disabled  Disabled flag
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        repaint();
    }

    /**
     * @param boardListener Listener called on game board state change
     */
    public void addBoardListener(@NotNull BoardListener boardListener) {
        this.boardListener = boardListener;
    }

    /**
     * Trigger slide, if user not change any slide do not repaint
     *
     * @param e Mouse event
     */
    private void triggerSlide(MouseEvent e) {
        if (disabled)
            return;

        final boolean slided = grid.slide(
                getPuzzleIndex(e)
        );
        if (!slided)
            return;

        repaint();
        if (grid.isOrdered()) {
            if (boardListener != null)
                boardListener.puzzleOrdered();

            JOptionPane.showMessageDialog(
                    this,
                    Resources.Translations.getString("win_dialog"),
                    Resources.Translations.getString("win_title"),
                    JOptionPane.INFORMATION_MESSAGE,
                    Resources.getIcon("checked")
            );
        }
    }

    /**
     * Reorder puzzles and repaint component
     */
    void shuffle() {
        grid.shuffle();
        repaint();
    }

    /**
     * @param e Mouse click / move event
     * @return  Slide index inside array
     */
    private Point getPuzzleIndex(MouseEvent e) {
        return new Point(
            e.getX() / getPuzzleSize().width,
            e.getY() / getPuzzleSize().height
        );
    }

    /**
     * @return  Single puzzle size relative to JPanel board size, its not image size
     */
    private Dimension getPuzzleSize() {
        final Dimension GRID_SIZE = grid.getPuzzles().getSize();
        return new Dimension(
                getSize().width / GRID_SIZE.width,
                getSize().height / GRID_SIZE.height
        );
    }

    /**
     * @return  Shuffled random tile with image
     */
    private static PuzzleGrid getRandomPuzzleGrid() {
        ImageTile tile = null;

        try {
            tile = new ImageTile(
                    Resources.getImage("puzzle-image.jpg"),
                    BOARD_SIZE,
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PuzzleGrid(tile);
    }

    /**
     * @param alpha Opacity value between 0-1
     * @return      Composite object
     */
    private static AlphaComposite makeAlphaComposite(@NotNull float alpha) {
        return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    /**
     * Draw puzzle grid
     *
     * @param g Graphics
     */
    @Override
    public void paint (Graphics g) {
        if (grid == null)
            return;

        super.paint(g);

        /**
         * Draw background
         */
        final Graphics2D imgContext = (Graphics2D) g.create();
        imgContext.setComposite(GameBoard.makeAlphaComposite(.15f));
        imgContext.drawImage(
            grid.getTile().getImage(),
                0, 0,
                getSize().width,
                getSize().height,
                null
        );
        imgContext.dispose();

        /**
         * Draw game grid
         */
        final Dimension puzzleSize = getPuzzleSize();
        final Graphics2D g2 = (Graphics2D) g;

        grid.getPuzzles().map((puzzle, x, y) -> {
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

        /**
         * Draw disabled layer
         */
        if (disabled) {
            final Graphics2D layerContext = (Graphics2D) g.create();
            layerContext.setColor(new Color(255, 255, 255, 160));
            layerContext.fillRect(0, 0, getWidth(), getHeight());
            layerContext.dispose();
        }
    }
}

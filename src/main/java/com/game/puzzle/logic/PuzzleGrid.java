package com.game.puzzle.logic;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumSet;

/**
 * Metadata representation of whole puzzle grid,
 * any grid logic should be placed there
 */
public class PuzzleGrid {
    public enum Direction {
        TOP(0, -1),
        BOTTOM(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        public final int offsetX;
        public final int offsetY;

        Direction(int offsetX, int offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }
    }

    private ImageTile tile;
    private ArrayIterator<PuzzleDescription> puzzles;

    /**
     * @param tile  Tile of images
     */
    public PuzzleGrid(@NotNull ImageTile tile) {
        this.tile = tile;
        this.puzzles = PuzzleGrid.extractPuzzles(tile);
        this.shuffle();
    }

    public ImageTile getTile() { return tile; }

    public ArrayIterator<PuzzleDescription> getPuzzles() { return this.puzzles; }

    /**
     * Reorder slides, be sure that null field is
     * in the right bottom corner
     */
    public void shuffle() {
        this.puzzles.shuffle();

        /**
         * at least one null slide have to
         * be placed in right corner
         */
        Point nullSlide = this.puzzles.find((element, x, y) -> element == null);
        if (nullSlide != null) {
            this.puzzles.array[nullSlide.y][nullSlide.x] = this.puzzles.array[this.puzzles.getSize().height - 1][this.puzzles.getSize().width -  1];
            this.puzzles.array[this.puzzles.getSize().height - 1][this.puzzles.getSize().width -  1] = null;
        }
    }

    /**
     * Slide puzzle, swap with first found null place
     *
     * @param p Slide coordinate
     * @return  True if slide is success
     */
    public boolean slide(Point p) throws ArrayIndexOutOfBoundsException {
        if (p.x >= this.puzzles.getSize().width || p.y >= this.puzzles.getSize().height)
            return false;

        /** if there is no free edges abort sliding */
        final EnumSet<Direction> movableEdges = this.getMovableEdges(p);
        if (movableEdges.isEmpty())
            return false;

        /** try to move first found edge, maybe there will be multiple move field in future */
        final Direction direction = movableEdges
                .iterator()
                .next();

        /** swap is always null, do not create temp variable */
        this.puzzles.array[p.y + direction.offsetY][p.x + direction.offsetX] = this.puzzles.array[p.y][p.x];
        this.puzzles.array[p.y][p.x] = null;
        return true;
    }

    /**
     * @param p Slide coordinate
     * @return  True if edge can be moveable
     */
    private EnumSet<Direction> getMovableEdges(Point p) {
        final Dimension boardSize = this.puzzles.getSize();
        EnumSet<Direction> movableEdges = EnumSet.noneOf(Direction.class);

        for (Direction dir : Direction.values()) {
            if (dir.offsetX + p.x < boardSize.width
                    && dir.offsetY + p.y < boardSize.height
                    && dir.offsetX + p.x >= 0
                    && dir.offsetY + p.y >= 0
                    && this.puzzles.array[p.y + dir.offsetY][p.x + dir.offsetX] == null) {
                movableEdges.add(dir);
            }
        }

        return movableEdges;
    }

    /**
     * @param tile  Tile of images
     */
    private static ArrayIterator<PuzzleDescription> extractPuzzles(@NotNull ImageTile tile) {
        final ArrayIterator<BufferedImage> tiles = tile.getTiles();
        final Dimension size = tiles.getSize();
        final ArrayIterator<PuzzleDescription> puzzles = new ArrayIterator<>(PuzzleDescription.class, size);

        tiles.map((item, x, y) -> {
            /**
             * Always one slide in puzzle game should be empty,
             * mark it as null value and do not create PuzzleDescription object
             */
            final PuzzleDescription puzzle = x == size.width - 1 && y == size.height - 1
                ? null
                : new PuzzleDescription(
                        y * size.width + x,
                        item
                );

            puzzles.array[y][x] = puzzle;
            return null;
        });

        return puzzles;
    }
}

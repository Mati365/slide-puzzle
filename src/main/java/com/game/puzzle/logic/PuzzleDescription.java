package com.game.puzzle.logic;

import javax.validation.constraints.NotNull;
import java.awt.Image;

/**
 * Metadata about single puzzle, it
 * should not contain any mutations
 * on puzzle data, it should be done
 * via PuzzleGrid
 */
public class PuzzleDescription {
    private int number;
    private Image image;

    public PuzzleDescription(int number, @NotNull Image image) {
        this.number = number;
        this.image = image;
    }

    public Image getImage() { return this.image; }

    public int getNumber() { return this.number; }
}

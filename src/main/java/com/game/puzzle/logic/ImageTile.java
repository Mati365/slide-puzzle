package com.game.puzzle.logic;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Cuts image into parts
 */
public class ImageTile {
    private Image image;
    private ArrayIterator<BufferedImage> tiles;

    public ImageTile(
            @NotNull Image image,
            @NotNull Dimension parts,
            Dimension scaledSize) throws Exception {
        this.image = image;
        this.tiles = ImageTile.cutImageIntoParts(
                image,
                parts,
                scaledSize
        );
    }

    public Image getImage() { return this.image; }

    public ArrayIterator<BufferedImage> getTiles() { return this.tiles; }

    /**
     * Tests dimensions object
     * @param dimension Tested Dimension object
     * @return Returns true if dimensions is valid
     */
    private static boolean isValidDimension(Dimension dimension) {
        return dimension != null && dimension.getWidth() * dimension.getHeight() > 0;
    }

    /**
     * Cut provided image into parts and try to scale to provided size
     *
     * @param image         Source image
     * @param parts         Total number of parts to be cutted
     * @param scaledSize    Size of total tile object
     * @return              Array of tiles
     * @throws Exception    Raises if wrong size is provided
     */
    private static ArrayIterator<BufferedImage> cutImageIntoParts(
            @NotNull Image image,
            @NotNull Dimension parts,
            Dimension scaledSize) throws Exception {
        if (!ImageTile.isValidDimension(parts))
            throw new Exception("Tile has been created with wrong dimensions!");

        /**
         * If scaled size is null try to rescale to
         * original image size
         */
        if (scaledSize == null)
            scaledSize = new Dimension(image.getWidth(null), image.getHeight(null));

        /**
         * Size of cell from source, before scale
         */
        final Dimension SOURCE_CELL_SIZE = new Dimension(
                image.getWidth(null) / parts.width,
                image.getHeight(null) / parts.height
        );

        /**
         * Single already scaled tiles cell, it has size
         * calc from scaledSize property
         */
        final Dimension CELL_SIZE = new Dimension(
                scaledSize.width /  parts.width,
                scaledSize.height / parts.height
        );

        /**
         * Prevent cache misses - order page:
         * - i is row / y axis
         * - j is column / x axis
         */
        return new ArrayIterator<>(BufferedImage.class, parts).map((element, x, y) -> {
            final BufferedImage img = new BufferedImage(
                    CELL_SIZE.width,
                    CELL_SIZE.height,
                    BufferedImage.TYPE_INT_RGB
            );
            final Graphics ctx = img.getGraphics();

            ctx.drawImage(
                    image,
                    0, 0, CELL_SIZE.width, CELL_SIZE.height,
                    SOURCE_CELL_SIZE.width * x,
                    SOURCE_CELL_SIZE.height * y,
                    (SOURCE_CELL_SIZE.width * x) + SOURCE_CELL_SIZE.width,
                    (SOURCE_CELL_SIZE.height * y) + CELL_SIZE.height,
                    null
            );

            return img;
        });
    }
}

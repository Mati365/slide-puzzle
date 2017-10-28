package com.game.puzzle.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.validation.constraints.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Resources {
    static class Translations {
        private static ResourceBundle messages = ResourceBundle.getBundle("locale", Locale.getDefault());

        /**
         * @param key   translated keyword path
         * @return      single translation from bundle
         */
        public static String getString(@NotNull String key) {
            return messages.getString(key);
        }

        /**
         * @param key   translated keyword path
         * @param args  formatter args
         * @return      single formatted translation from bundle
         */
        public static String getString(@NotNull String key, Object... args) {
            return MessageFormat.format(Translations.getString(key), args);
        }
    }

    /**
     * @param resource  Resource path to be appended to /images/ path
     * @return  Loaded image, null if not present
     */
    public static BufferedImage getImage(@NotNull String resource) {
        final URL imageResource = Resources.class.getResource("/images/" + resource);
        BufferedImage image = null;

        try {
            image = ImageIO.read(imageResource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * @param icon  PNG icons name inside images/icons/ folder
     * @return  Loaded ImageIcon object
     */
    public static ImageIcon getIcon(@NotNull String icon) {
        return new ImageIcon(
                Resources.getImage("icons/" + icon + ".png")
        );
    }
}

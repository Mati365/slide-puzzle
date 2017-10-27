package com.game.puzzle.ui;

import javax.validation.constraints.NotNull;

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
}

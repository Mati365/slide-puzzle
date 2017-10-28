package com.game.puzzle.ui;

import javax.swing.*;
import java.awt.Dimension;

/**
 * Main game window it should only initialize
 * window and load GamePanel JPanel, do not
 * place there any logic
 */
public class GameWindow extends JFrame {
    private static final Dimension WINDOW_SIZE = new Dimension(402, 500);

    /**
     * Create base game window, set game layer
     * as default window layer, do not try to
     * extend JFrame window object
     */
    static void createWindow() {
        JFrame window = new JFrame(Resources.Translations.getString("app_name"));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(GameWindow.WINDOW_SIZE);
        window.setContentPane(new GamePanel());
        window.pack();
        window.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                GameWindow.createWindow();
            }
        });
    }
}

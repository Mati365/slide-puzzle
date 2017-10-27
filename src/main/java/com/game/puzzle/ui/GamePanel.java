package com.game.puzzle.ui;

import com.game.puzzle.logic.SecondsTimer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main game layer
 */
public class GamePanel extends JPanel {
    public GamePanel() {
        super();
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.createLayout();
    }

    /**
     * Create whole Panel UI
     */
    private void createLayout() {
        this.add(this.createTimeTooltip(), BorderLayout.NORTH);
        this.add(new GameBoard(), BorderLayout.CENTER);
    }

    /**
     * Creates time tooltip with timer thread
     * @return  Instance of JLabel Swing component
     */
    private JLabel createTimeTooltip() {
        JLabel timeTooltip = new JLabel("0", SwingConstants.CENTER);
        timeTooltip.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 10, 0)
        );
        new SecondsTimer(seconds -> {
            SwingUtilities.invokeLater(
                    () -> timeTooltip.setText(Resources.Translations.getString("game_time", seconds))
            );
            return false;
        });
        return timeTooltip;
    }
}

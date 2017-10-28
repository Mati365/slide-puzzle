package com.game.puzzle.ui;

import com.game.puzzle.logic.SecondsTimer;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

/**
 * Main game layer
 */
public class GamePanel extends JPanel {
    private SecondsTimer timer = null;
    private GameBoard board = new GameBoard();

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
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(this.createTimeTooltip(), BorderLayout.WEST);
        topBar.add(this.createShuffleButton(), BorderLayout.EAST);
        topBar.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 5, 0)
        );

        this.add(topBar, BorderLayout.NORTH);
        this.add(this.board, BorderLayout.CENTER);
    }

    /**
     * Creates time tooltip with timer thread
     * @return  Instance of JLabel Swing component
     */
    private JLabel createTimeTooltip() {
        JLabel timeTooltip = new JLabel("0", SwingConstants.CENTER);
        timeTooltip.setIconTextGap(10);
        timeTooltip.setIcon(
                Resources.getIcon("wall-clock")
        );

        this.timer = new SecondsTimer(seconds -> {
            SwingUtilities.invokeLater(
                    () -> timeTooltip.setText(Resources.Translations.getString("game_time", seconds))
            );
            return false;
        });
        return timeTooltip;
    }

    /**
     * @return  Button object
     */
    private JButton createShuffleButton() {
        final JButton shuffleButton = new JButton();

        shuffleButton.setBorderPainted(false);
        shuffleButton.setIcon(
                Resources.getIcon("refresh")
        );
        shuffleButton.setPreferredSize(
                new Dimension(32, 32)
        );
        shuffleButton.addActionListener(
                (e) -> {
                    if (this.timer != null)
                        this.timer.resetTimer();

                    this.board.shuffle();
                }
        );

        return shuffleButton;
    }
}

package com.game.puzzle.ui;

import com.game.puzzle.logic.SecondsTimer;

import javax.swing.*;
import java.awt.*;
import javax.validation.constraints.NotNull;
import javax.swing.border.EmptyBorder;

/**
 * Simple class that helps with
 * creation toolbar buttons in GamePanel
 */
class IconButton extends JButton {
    private static final Dimension ICON_SIZE = new Dimension(32, 32);

    public IconButton(@NotNull String icon) {
        super();
        setBorderPainted(false);
        setIcon(Resources.getIcon(icon));
        setPreferredSize(IconButton.ICON_SIZE);
    }

    /**
     * @param icon  Icon path from resources/image folder
     */
    public void setIcon(@NotNull String icon) {
        setIcon(
                Resources.getIcon(icon)
        );
    }
}

/**
 * Main game layer
 */
public class GamePanel extends JPanel {
    private GameBoard board = new GameBoard(true);
    private SecondsTimer timer = null;

    private JLabel timeTooltip = null;
    private IconButton shuffleButton = null;
    private IconButton stopButton = null;

    public GamePanel() {
        super();
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        createLayout();

        board.addBoardListener(this::stopTimer);
    }

    /**
     * @param seconds   Elapsed seconds count
     * @return          Translated elapsed time
     */
    private static String getTimeTranslation(long seconds) {
        return Resources.Translations.getString("game_time", seconds);
    }

    /**
     * Starts new timer if previous is stopped
     */
    private void startTimer() {
        if (timer != null)
            timer.stop();

        board.setDisabled(false);

        stopButton.setVisible(true);
        shuffleButton.setIcon("refresh");

        timer = new SecondsTimer(seconds -> {
            SwingUtilities.invokeLater(
                    () -> timeTooltip.setText(GamePanel.getTimeTranslation(seconds))
            );
            return false;
        });
    }

    /**
     * Kills timer and disable board
     */
    private void stopTimer() {
        board.setDisabled(true);

        stopButton.setVisible(false);
        shuffleButton.setIcon("play-button");

        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }


    /**
     * Create whole Panel UI
     */
    private void createLayout() {
        shuffleButton = new IconButton("play-button");
        shuffleButton.addActionListener(
                (e) -> {
                    board.shuffle();
                    startTimer();
                }
        );

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(createToolbar(), BorderLayout.WEST);
        topBar.add(shuffleButton, BorderLayout.EAST);
        topBar.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 5, 0)
        );

        add(topBar, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
    }

    private JPanel createToolbar() {
        timeTooltip = createTimeTooltip();

        stopButton = new IconButton("stop");
        stopButton.setVisible(false);
        stopButton.addActionListener(
                (e) -> stopTimer()
        );

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(stopButton);
        panel.add(timeTooltip);

        return panel;
    }

    /**
     * Creates time tooltip with timer thread
     * @return  Instance of JLabel Swing component
     */
    private JLabel createTimeTooltip() {
        JLabel timeTooltip = new JLabel(
                GamePanel.getTimeTranslation(0),
                SwingConstants.CENTER
        );

        timeTooltip.setIconTextGap(10);
        timeTooltip.setIcon(
                Resources.getIcon("wall-clock")
        );

        return timeTooltip;
    }
}

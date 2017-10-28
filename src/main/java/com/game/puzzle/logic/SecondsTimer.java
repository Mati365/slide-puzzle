package com.game.puzzle.logic;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.concurrent.*;

/**
 * Simple timer that ticks only one per second
 */
public class SecondsTimer {
    @FunctionalInterface
    public interface TimerHandler {
        /**
         * Callback called after timer has been updated
         *
         * @param seconds   Total seconds elapsed from timer start
         * @return If true stop the timer
         */
        boolean timerUpdated(long seconds);
    }

    private long seconds = 0;
    private ScheduledExecutorService executor = null;
    private TimerHandler handler = null;

    public SecondsTimer(@NotNull TimerHandler handler) {
        this.handler = handler;
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleAtFixedRate(() -> {
            handler.timerUpdated(++seconds);
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Sets remain seconds to 0
     */
    public void resetTimer() {
        this.seconds = 0;
        this.handler.timerUpdated(this.seconds);
    }

    /**
     * Stop timer and resets values
     */
    public void stop() {
        this.executor.shutdown();
        this.resetTimer();
    }
}

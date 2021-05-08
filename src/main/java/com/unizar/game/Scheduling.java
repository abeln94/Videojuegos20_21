package com.unizar.game;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Allows to schedule a function to run later.
 */
public class Scheduling {

    // ------------------------- data -------------------------

    final private Runnable runnable;
    private final long delay;
    private Timer timer = new Timer();

    /**
     * Prepares a scheduling object that will run the runnable delay milliseconds after calling schedule
     * Creating this object does not schedule anything
     *
     * @param runnable runnable to run
     * @param delay    milliseconds to wait until run
     */
    public Scheduling(Runnable runnable, long delay) {
        this.runnable = runnable;
        this.delay = delay;
    }

    /**
     * Schedules the runnable to run in delay milliseconds.
     * After running it won't be run again
     */
    public void schedule() {
        cancel();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delay);
    }

    /**
     * Cancels the current scheduled runnable (if any)
     */
    public void cancel() {
        timer.cancel();
        timer = new Timer();
    }
}

package com.fr.design.mainframe.template.info;

/**
 * Created by plough on 2019/4/19.
 */
public class TimeConsumeTimer {
    private static final int ONE_THOUSAND = 1000;
    private enum State {
        RUNNING, STOPPED
    }
    private int timeConsume;  // 单位 s
    private long startMS;  // 单位 ms
    private long stopMS;
    private State state;
    private boolean enabled;

    public TimeConsumeTimer() {
        reset();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void start() {
        if (!isEnabled() || isRunning()) {
            return;
        }
        startMS = System.currentTimeMillis();
        state = State.RUNNING;
    }

    public void stop() {
        if (!isEnabled() || !isRunning()) {
            return;
        }
        stopMS = System.currentTimeMillis();

        timeConsume += ((stopMS - startMS) / ONE_THOUSAND);
        startMS = 0;
        stopMS = 0;
        state = State.STOPPED;


        System.out.println("timeConsume now: " + timeConsume);
    }

    public int popTime() {
        if (!isEnabled()) {
            return 0;
        }
        stop();
        int result = timeConsume;
        reset();
        return result;
    }

    private boolean isRunning() {
        return state == State.RUNNING;
    }

    private void reset() {
        timeConsume = 0;
        startMS = 0;
        stopMS = 0;
        state = State.STOPPED;
    }
}

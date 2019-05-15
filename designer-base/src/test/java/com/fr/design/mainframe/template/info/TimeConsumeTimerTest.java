package com.fr.design.mainframe.template.info;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by plough on 2019/4/19.
 */
public class TimeConsumeTimerTest {

    @Test
    public void testNotEnabled() throws InterruptedException {
        TimeConsumeTimer consumeTimer = new TimeConsumeTimer();
        consumeTimer.start();
        Thread.sleep(1100);
        consumeTimer.stop();
        assertEquals(0, consumeTimer.popTime());
    }

    @Test
    public void testEnabled() throws InterruptedException {
        TimeConsumeTimer consumeTimer = new TimeConsumeTimer();
        consumeTimer.setEnabled(true);
        consumeTimer.start();
        Thread.sleep(1100);
        consumeTimer.stop();
        assertEquals(1, consumeTimer.popTime());
        assertEquals(0, consumeTimer.popTime());
    }

    @Test
    public void testMultiTimes() throws InterruptedException {
        TimeConsumeTimer consumeTimer = new TimeConsumeTimer();
        consumeTimer.setEnabled(true);

        consumeTimer.start();
        Thread.sleep(1010);
        consumeTimer.stop();

        Thread.sleep(2000);

        consumeTimer.start();
        Thread.sleep(1010);
        assertEquals(2, consumeTimer.popTime());
    }

    @Test
    public void testStartMultiTime() throws InterruptedException {
        TimeConsumeTimer consumeTimer = new TimeConsumeTimer();
        consumeTimer.setEnabled(true);

        consumeTimer.start();
        Thread.sleep(1010);
        consumeTimer.start();
        Thread.sleep(1010);
        consumeTimer.start();
        Thread.sleep(1010);

        assertEquals(3, consumeTimer.popTime());
    }
}

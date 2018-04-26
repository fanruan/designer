package com.fr.design.gui.iprogressbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-6-5
 * Time: 下午3:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class AutoProgressBar implements MonitorCancelWork{
    private ProgressMonitor monitor;
    private Timer timer;
    private int i;

    public AutoProgressBar(Component parentComponent, Object message, String note, int min, int max) {
        monitor = new ProgressMonitor(parentComponent, message, note, min, max);
        monitor.setProgress(0);
        monitor.setMillisToDecideToPopup(0);
        timer  = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                i = i > 99 ? 0 : i;
                monitor.setProgress(i);
                i++;
                if (monitor.isCanceled()) {
                    doMonitorCanceled();
                    timer.stop();
                }
            }
        });
    }

    public void start() {
        i = 0;
        timer.start();
    }

    public void close() {
        timer.stop();
        monitor.setProgress(100);
        monitor.close();
    }


}
package com.fr.design.mainframe.alphafine.listener;

import com.sun.awt.AWTUtilities;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by XiaXiang on 2017/5/4.
 */
public class ComponentHandler extends ComponentAdapter {
    public void componentResized(ComponentEvent e) {
        Window win = (Window) e.getSource();
        Frame frame = (win instanceof Frame) ? (Frame) win : null;
        if ((frame != null)
                && ((frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0)) {
            AWTUtilities.setWindowShape(win, null);
        } else {
            /** 设置圆角 */
            AWTUtilities.setWindowShape(win,
                    new RoundRectangle2D.Double(0.0D, 0.0D, win.getWidth(),
                            win.getHeight(), 16.0D, 16.0D));
        }
    }
}


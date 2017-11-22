package com.fr.design.gui.icontainer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalScrollPaneUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-2-13
 * Time: 上午10:07
 */
public class UIScrollPaneUI extends MetalScrollPaneUI implements PropertyChangeListener {

    /**
     * 创建UI
     * @param c 组件
     * @return  组件UI
     */
    public static ComponentUI createUI(JComponent c) {
        return new UIScrollPaneUI();
    }

    /**
     * 为组件初始化UI
     * @param c 组件
     */
    public void installUI(JComponent c) {
        super.installUI(c);

        // Note: It never happened before Java 1.5 that scrollbar is null
        JScrollBar sb = scrollpane.getHorizontalScrollBar();
        if (sb != null) {
            sb.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, Boolean.FALSE);
        }

        sb = scrollpane.getVerticalScrollBar();
        if (sb != null) {
            sb.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, Boolean.FALSE);
        }
    }

    protected PropertyChangeListener createScrollBarSwapListener() {
        return this;
    }

    /**
     * 属性改变的方法
     * @param e 事件
     */
    public void propertyChange(PropertyChangeEvent e) {
    }
}
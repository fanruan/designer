package com.fr.plugin.chart.designer;

import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.iscrollbar.UIScrollBar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/8/11.
 */
public abstract class AbstractVanChartScrollPane<T> extends BasicScrollPane<T> {

    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, original));
        this.add(leftcontentPane);
    }

    protected void setLeftContentPaneBouns (Container parent, UIScrollBar scrollBar, int beginY, int maxheight) {
        int width = parent.getWidth();
        int height = parent.getHeight();
        if (leftcontentPane.getPreferredSize().height > maxheight) {
            leftcontentPane.setBounds(0, -beginY, width - scrollBar.getWidth(), height + beginY);
            scrollBar.setBounds(width - scrollBar.getWidth() - 1, 0, scrollBar.getWidth(), height);
        } else {
            leftcontentPane.setBounds(0, 0, width, height);
        }
    }
}

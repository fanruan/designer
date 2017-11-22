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
        leftcontentPane.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 10, original));
        this.add(leftcontentPane);
    }

    protected void setLeftContentPaneBounds(Container parent, UIScrollBar scrollBar, int beginY, int maxheight) {
        int width = parent.getWidth();
        int height = parent.getHeight();
        if (leftcontentPane.getPreferredSize().height > maxheight) {
            scrollBar.setBounds(width - scrollBar.getWidth() - 1, 0, 6, height);
            leftcontentPane.setBounds(0, -beginY, width - 6, height + beginY);
            leftcontentPane.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 4, original));

        } else {
            leftcontentPane.setBounds(0, 0, width, height);
            leftcontentPane.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 10, original));
        }
    }
    public void reloaPane(JPanel pane){
        super.reloaPane(pane);
        leftcontentPane.setBorder(BorderFactory.createEmptyBorder());
    }
}

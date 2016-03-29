package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;
import com.fr.design.dialog.MultiTabPane;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 16/3/9.
 */
public abstract class ChartTabPane extends MultiTabPane<Chart> {

    private static final long serialVersionUID = 8633385688766835240L;

    @Override
    protected void initLayout() {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 10, getBackground()));
        tabPanel.add(tabPane, BorderLayout.CENTER);
        this.setLayout(new BorderLayout(0, 4));
        this.add(tabPanel, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
    }

    @Override
    public boolean accept(Object ob) {
        return false;
    }

    @Override
    public String title4PopupWindow() {
        return StringUtils.EMPTY;
    }

    @Override
    public void reset() {
    }

    /**
     * 设置选中的界面id
     */
    public void setSelectedByIds(int level, String... id) {
        tabPane.setSelectedIndex(-1);
        for (int i = 0; i < paneList.size(); i++) {
            if (ComparatorUtils.equals(id[level], NameArray[i])) {
                tabPane.setSelectedIndex(i);
                tabPane.tabChanged(i);
                if (id.length >= level + 2) {
                    ((AutoSelectedPane)paneList.get(i)).setSelectedIndex(id[level + 1]);
                }
                break;
            }
        }
    }

}

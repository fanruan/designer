package com.fr.van.chart.custom.component;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.general.ComparatorUtils;
import com.fr.van.chart.designer.component.VanChartPlotMultiTabPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Created by Fangjie on 2016/4/22.
 */
public abstract class VanChartCustomPlotTabPane<E, T> extends VanChartPlotMultiTabPane<E, T> {
    private static final long serialVersionUID = 8633385688766835241L;

    public VanChartCustomPlotTabPane(E plot, BasicPane parent) {
        this(plot, parent, null);
    }

    public VanChartCustomPlotTabPane(E plot, BasicPane parent, AttributeChangeListener listener) {
        super(plot, parent, listener);
    }

    protected abstract void initTabTitle();

    @Override
    protected void initLayout() {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, getBackground()));
        tabPanel.add(tabPane, BorderLayout.CENTER);
        this.setLayout(new BorderLayout(0, 6));
        this.add(tabPanel, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
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

    @Override
    public void relayoutWhenListChange() {
        centerPane = new JPanel(cardLayout) {
            @Override
            public Dimension getPreferredSize() {
                if (tabPane.getSelectedIndex() == -1) {
                    return super.getPreferredSize();
                } else {
                    return paneList.get(tabPane.getSelectedIndex()).getPreferredSize();
                }
            }
        };

        //获取tab的标题
        initTabTitle();

        tabPane = new VanChartCustomPlotUITabGroup(NameArray) {
            @Override
            public void tabChanged(int index) {
                dealWithTabChanged(index);
            }
        };
        tabPane.setSelectedIndex(0);
        tabPane.tabChanged(0);
        initLayout();
    }
}

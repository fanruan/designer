package com.fr.extended.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UITabGroup;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.List;

/**
 * Created by shine on 2018/3/13.
 */
public abstract class AbstractExtendedStylePane<T extends AbstractChart> extends AbstractChartAttrPane {

    private UITabGroup tabPane;

    private String[] NameArray;
    private JPanel centerPane;
    private CardLayout cardLayout;

    private List<ExtendedScrollPane<T>> paneList;

    private AttributeChangeListener listener;

    private T chart;

    public AbstractExtendedStylePane() {
    }

    public AbstractExtendedStylePane(AttributeChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected JPanel createContentPane() {

        cardLayout = new CardLayout();
        paneList = initPaneList();

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
        NameArray = new String[paneList.size()];
        for (int i = 0; i < paneList.size(); i++) {
            BasicBeanPane<T> pane = paneList.get(i);
            NameArray[i] = pane.getTitle();
            centerPane.add(pane, NameArray[i]);
        }

        tabPane = new UITabGroup(NameArray) {
            @Override
            public void tabChanged(int index) {
                cardLayout.show(centerPane, NameArray[index]);
                dealWithTabChanged();
            }
        };
        tabPane.setSelectedIndex(0);
        tabPane.tabChanged(0);

        JPanel panel = new JPanel(new BorderLayout(0, 4));

        panel.add(tabPane, BorderLayout.NORTH);
        panel.add(centerPane, BorderLayout.CENTER);

        return panel;
    }

    private void dealWithTabChanged() {
        if (chart != null) {
            AbstractExtendedStylePane.this.removeAttributeChangeListener();
            paneList.get(tabPane.getSelectedIndex()).populateBean(chart);
            AbstractExtendedStylePane.this.addAttributeChangeListener(listener);
        }

    }

    protected abstract List<ExtendedScrollPane<T>> initPaneList();

    @Override
    public void populate(ChartCollection collection) {
        if (collection != null) {
            Chart chart = collection.getSelectedChart();

            if (chart != null && chart instanceof AbstractChart){
                this.chart = (T)chart;
                paneList.get(tabPane.getSelectedIndex()).populateBean(this.chart);
            }

        }

    }

    @Override
    public void update(ChartCollection collection) {
        if (collection != null) {
            Chart chart = collection.getSelectedChart();

            if (chart != null && chart instanceof AbstractChart){
                this.chart = (T)chart;
                paneList.get(tabPane.getSelectedIndex()).updateBean(this.chart);
            }

        }
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/chart/ChartStyle.png";
    }

    @Override
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_TITLE;
    }

}

package com.fr.design.mainframe.chart.gui;


import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.data.CustomChartDataContentsPane;

import javax.swing.*;
import java.awt.*;


/**
 * Created by mengao on 2017/4/26.
 */
public class CustomChartDataPane extends ChartDataPane {
    private boolean supportCellData = true;

    public CustomChartDataPane(AttributeChangeListener listener) {
        super(listener);
        this.listener = listener;
    }

    @Override
    protected JPanel createContentPane() {
        contentsPane = new CustomChartDataContentsPane(listener, CustomChartDataPane.this);
        return contentsPane;
    }

    protected void repeatLayout(ChartCollection collection) {
        Plot plot =collection.getSelectedChart().getPlot();
        if (contentsPane != null) {
            this.remove(contentsPane);
        }

        this.setLayout(new BorderLayout(0, 0));
        contentsPane = new CustomChartDataContentsPane(listener, CustomChartDataPane.this ,plot);

        if (contentsPane != null) {
            contentsPane.setSupportCellData(supportCellData);
        }
    }

}

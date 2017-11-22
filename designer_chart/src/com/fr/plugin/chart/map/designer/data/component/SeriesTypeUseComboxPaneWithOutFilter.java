package com.fr.plugin.chart.map.designer.data.component;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.SeriesNameUseFieldNamePane;
import com.fr.design.mainframe.chart.gui.data.table.SeriesNameUseFieldValuePane;
import com.fr.design.mainframe.chart.gui.data.table.SeriesTypeUseComboxPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 16/5/16.
 */
public class SeriesTypeUseComboxPaneWithOutFilter extends SeriesTypeUseComboxPane {

    public SeriesTypeUseComboxPaneWithOutFilter(ChartDataPane parent, Plot initplot) {
        super(parent, initplot);
    }

    protected void initLayout() {
        super.initLayout();
        this.add(new JPanel(), BorderLayout.SOUTH);
    }

    protected void initComponents() {
        super.initComponents();
        this.setSelectedIndex(1);
    }

    @Override
    protected SeriesNameUseFieldValuePane createValuePane() {
        return new SeriesNameUseFieldValuePaneWithOutFilter();
    }

    @Override
    protected SeriesNameUseFieldNamePane createNamePane() {
        return new SeriesNameUseFieldNamePaneWithOutFilter();
    }
}

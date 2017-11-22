package com.fr.plugin.chart.gantt.designer.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hufan on 2017/1/10.
 */
public class VanChartGanttDataPane extends ChartDataPane {
    public VanChartGanttDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected JPanel createContentPane() {
        contentsPane = new VanChartGanttContentsPane(listener, VanChartGanttDataPane.this);
        return contentsPane;
    }

    //编辑内置数据集会stateChange,会调用这里
    @Override
    protected void repeatLayout(ChartCollection collection) {
        if(contentsPane != null) {
            this.remove(contentsPane);
        }
        this.setLayout(new BorderLayout(0, 0));

        contentsPane = new VanChartGanttContentsPane(listener, VanChartGanttDataPane.this);
        contentsPane.setSupportCellData(isSupportCellData());

        this.add(contentsPane, BorderLayout.CENTER);
    }
}

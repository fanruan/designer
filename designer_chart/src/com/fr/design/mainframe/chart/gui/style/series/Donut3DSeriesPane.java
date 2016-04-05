package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

import javax.swing.*;

/**
 * Created by Mitisky on 16/4/5.
 */
public class Donut3DSeriesPane extends AbstractPlotSeriesPane {
    public Donut3DSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }


    /**
     * 在每个不同类型Plot, 得到不同类型的属性. 比如: 柱形的风格, 折线的线型曲线.
     */
    @Override
    protected JPanel getContentInPlotType() {
        return null;
    }
}

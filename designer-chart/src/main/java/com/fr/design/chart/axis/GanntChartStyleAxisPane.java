package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartCategoryPane;
import com.fr.general.Inter;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-6
 * Time   : 上午9:50
 */
public class GanntChartStyleAxisPane extends BinaryChartStyleAxisPane {
    public GanntChartStyleAxisPane(Plot plot) {
        super(plot);
    }

    protected AxisStyleObject getXAxisPane(Plot plot) {
    	ChartCategoryPane categoryPane = new ChartCategoryPane();
    	categoryPane.getAxisValueTypePane().removeTextAxisPane();
        return new AxisStyleObject(Inter.getLocText("Chart_Date_Axis"), categoryPane);
    }

    protected AxisStyleObject getYAxisPane(Plot plot) {
        return new AxisStyleObject(CATE_AXIS, new ChartCategoryPane());
    }
}
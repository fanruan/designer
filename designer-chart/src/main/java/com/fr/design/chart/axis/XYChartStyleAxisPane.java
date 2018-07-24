package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartValuePane;


/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-6
 * Time   : 上午9:51
 */
public class XYChartStyleAxisPane extends BinaryChartStyleAxisPane {
    public XYChartStyleAxisPane(Plot plot) {
        super(plot);
    }

    protected AxisStyleObject getXAxisPane(Plot plot) {
        return new AxisStyleObject(com.fr.design.i18n.Toolkit.i18nText("ChartF-X_Axis"), new ChartValuePane());
    }

    protected AxisStyleObject getYAxisPane(Plot plot) {
        return new AxisStyleObject(com.fr.design.i18n.Toolkit.i18nText("ChartF-Y_Axis"), new ChartValuePane());
    }
}
package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartValuePane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-6
 * Time   : 上午9:02
 */
public class ValueChartStyleAxisPane extends ChartStyleAxisPane {
	private static final long serialVersionUID = 8513685484255363315L;

	public ValueChartStyleAxisPane(Plot plot) {
        super(plot);
    }

    public final AxisStyleObject[] createAxisStyleObjects(Plot plot) {
        return new AxisStyleObject[]{new AxisStyleObject(
                CATE_AXIS,
                new ChartValuePane())};
    }
}
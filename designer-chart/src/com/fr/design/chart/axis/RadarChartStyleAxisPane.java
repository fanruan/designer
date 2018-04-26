package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartRadarPane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-5
 * Time   : 下午7:18
 * 雷达图的图表样式--> 坐标轴界面
 */
public class RadarChartStyleAxisPane extends ChartStyleAxisPane {
	private static final long serialVersionUID = 8919941507725513032L;

	public RadarChartStyleAxisPane(Plot plot) {
        super(plot);
    }

    public final AxisStyleObject[] createAxisStyleObjects(Plot plot) {
        return new AxisStyleObject[]{new AxisStyleObject(
                CATE_AXIS,
                new ChartRadarPane())};
    }
}
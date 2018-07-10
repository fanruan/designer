package com.fr.van.chart.designer.style.axis;

import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.van.chart.designer.style.VanChartStylePane;

/**
 * 坐标轴界面接口。包括有类型选择的和数值轴
 */
public interface VanChartXYAxisPaneInterface {

    public void populate(VanChartAxis axis);

    public VanChartAxis update(VanChartAxis axis) ;

    public void setParentPane(VanChartStylePane parent);
}
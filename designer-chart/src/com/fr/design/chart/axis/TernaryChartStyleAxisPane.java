package com.fr.design.chart.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.chart.gui.style.axis.ChartCategoryPane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartSecondValuePane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartValuePane;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-6
 * Time   : 上午8:58
 * 三元坐标设置界面
 */
public class TernaryChartStyleAxisPane extends ChartStyleAxisPane {
	private static final long serialVersionUID = 539802340384090324L;

	public TernaryChartStyleAxisPane(Plot plot) {
        super(plot);
    }

    @Override
    public AxisStyleObject[] createAxisStyleObjects(Plot plot) {
        return new AxisStyleObject[]{
                new AxisStyleObject(CATE_AXIS,
                        new ChartCategoryPane()),
                new AxisStyleObject(getValueAxisName(),
                        new ChartValuePane()),
                new AxisStyleObject(getSecondValueAxisName(),
                        new ChartSecondValuePane())
        };
    }
    
    protected String getValueAxisName() {
    	return VALUE_AXIS;
    }
    
    protected String getSecondValueAxisName() {
    	return SECOND_AXIS;
    }
}
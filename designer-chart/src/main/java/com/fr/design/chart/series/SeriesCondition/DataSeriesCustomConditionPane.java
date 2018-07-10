package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.chartattr.CustomPlot;
import com.fr.chart.chartattr.Plot;

/**
 * 类说明 组合图 条件属性界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-8-19 上午09:50:05
 */
public class DataSeriesCustomConditionPane extends DataSeriesConditionPane{
	private static final long serialVersionUID = -6568508006201353211L;

    /**
     * 返回class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return CustomPlot.class;
    }
}
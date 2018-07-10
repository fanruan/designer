package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;


/**
 * 饼图 条件显示 参数.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-11 下午03:48:32
 */
public class PiePlotChartConditionPane extends ChartConditionPane {

	  public String[] columns2Populate() {
	        return new String[]{
	                ChartConstants.SERIES_INDEX,
	                ChartConstants.SERIES_NAME,
	                ChartConstants.VALUE
	        };
	    }
}
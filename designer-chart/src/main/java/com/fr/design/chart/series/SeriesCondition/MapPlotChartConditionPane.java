package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.ChartConstants;

/**
 * 地图的条件 参数下拉.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-11 下午03:44:40
 */
public class MapPlotChartConditionPane extends ChartConditionPane {

	  public String[] columns2Populate() {
	        return new String[]{
	                ChartConstants.AREA_NAME,
	                ChartConstants.AREA_VALUE
	        };
	    }
}
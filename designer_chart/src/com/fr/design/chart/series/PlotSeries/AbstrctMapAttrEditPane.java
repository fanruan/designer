package com.fr.design.chart.series.PlotSeries;

import com.fr.chart.base.MapSvgAttr;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public interface AbstrctMapAttrEditPane {

    /**
     * 更新界面
     * @param attr  地图属性
     */
    public void populateMapAttr(MapSvgAttr attr);

    /**
     * 更新MapSvgAttr
     * @return  返回属性
     */
    public MapSvgAttr updateCurrentAttr();
}
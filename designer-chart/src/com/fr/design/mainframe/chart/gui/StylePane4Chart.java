package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.style.series.ChartSeriesPane;
import com.fr.design.mainframe.chart.gui.style.series.SeriesPane4ChartDesigner;

/**
 * 图表设计器的样式面板
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-25
 * Time: 下午6:56
 */
public class StylePane4Chart extends ChartStylePane {

    public StylePane4Chart(AttributeChangeListener listener, boolean isNeedFormula) {
        super(listener, isNeedFormula);
    }

    public void update(ChartCollection collection) {
        int selectIndex = collection.getSelectedIndex();
        super.update(collection);
        collection.getSelectedChart().setStyleGlobal(false);
        collection.setChartName(selectIndex, collection.getSelectedChart().getTitle().getTextObject().toString());
    }

    protected ChartSeriesPane createChartSeriesPane(){
   		return new SeriesPane4ChartDesigner(StylePane4Chart.this);
   	}

}
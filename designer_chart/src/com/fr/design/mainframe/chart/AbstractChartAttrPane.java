package com.fr.design.mainframe.chart;


import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;

public abstract class AbstractChartAttrPane extends AbstractAttrNoScrollPane {
	public abstract void populate(ChartCollection collection);

	public abstract void update(ChartCollection collection);

	public void populateBean(ChartCollection collection) {
		if (collection == null) {
			return;
		}
		removeAttributeChangeListener();
		populate(collection);
	}
	
	/**
	 * 注册图表切换按钮 改变的 事件. 
	 * @param isFromChartHyper 编辑面板
	 */
	public void registerChartEditPane(ChartEditPane isFromChartHyper) {
		
	}

	/**
	 * 刷新图表数据界面
	 * @param collection 图表收集器
	 */
	public void refreshChartDataPane(ChartCollection collection){

	}
}
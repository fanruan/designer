package com.fr.design.gui.chart;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.designer.TargetComponent;
import com.fr.design.mainframe.DockingView;

/**
 * 图表属性界面 抽象, 用于多工程协作..
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-7-10 上午09:12:11
 */
public abstract class BaseChartPropertyPane extends DockingView {
	
	public abstract void setSureProperty();

	/**
	 * 设置是否支持单元格数据.
	 */
	public abstract void setSupportCellData(boolean supportCellData);
	
	public abstract void populateChartPropertyPane(BaseChartCollection collection, TargetComponent<?> ePane);

	public abstract ChartEditPaneProvider getChartEditPane();

	public abstract void addChartEditPane(String plotID);

	//public abstract void clear();
}
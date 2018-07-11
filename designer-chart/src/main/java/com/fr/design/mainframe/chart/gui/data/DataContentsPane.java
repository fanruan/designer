package com.fr.design.mainframe.chart.gui.data;

import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;

/**
 * 图表数据内容界面, 属性表 样式数据.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-18 下午03:42:20
 */
public abstract class DataContentsPane extends AbstractChartAttrPane {


	/**
	 * 界面标题
	 */
	public String getIconPath() {
		return "com/fr/design/images/chart/ChartData.png";
	}

	/**
	 * 界面标题
	 */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_DATA_TITLE;
	}
	
	/**
	 * 设置是否关联单元格数据.
	 */
	public abstract void setSupportCellData(boolean surpportCellData);
	
	/**
	 * 清除掉响应事件.
	 */
//	public void removeAttributeListener() {
//		super.removeAttributeChangeListener();
//	}
}
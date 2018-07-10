package com.fr.design.mainframe.chart.gui.style.area;


import javax.swing.JPanel;

import com.fr.chart.chartattr.Plot;

/**
 * 属性表, 图表样式-区域-绘图区 间隔背景界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-9 下午12:28:47
 */
public abstract class ChartAxisAreaPane extends JPanel {
	/**
	 * 更新间隔背景界面.
	 */
	public abstract void populateBean(Plot plot);

	/**
	 * 保存间隔背景的界面属性.
	 */
	public abstract void updateBean(Plot plot);
}
package com.fr.design.mainframe.chart.gui.style.axis;

import javax.swing.JPanel;

/**
 * 属性表 继承的 百分比 值轴界面, 没有百分比.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-4 下午03:22:43
 */
public class ChartPercentValuePane extends ChartValuePane {
	
	// 没有百分比
	protected JPanel addLogarithmicPane2ValuePane() {
		return null;
    }
}
package com.fr.design.gui.chart;

import com.fr.base.chart.BaseChartCollection;
import com.fr.stable.core.PropertyChangeListener;

import javax.swing.JComponent;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-7-10 上午09:19:36
 */
public abstract class MiddleChartComponent extends JComponent {

	public abstract void populate(BaseChartCollection cc);

	public abstract BaseChartCollection update();
	
	public abstract void reset();

	public abstract void addStopEditingListener(PropertyChangeListener list);
}
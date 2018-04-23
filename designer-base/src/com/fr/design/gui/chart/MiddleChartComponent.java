package com.fr.design.gui.chart;

import javax.swing.JComponent;

import com.fr.base.chart.BaseChart;
import com.fr.base.chart.BaseChartCollection;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.stable.core.PropertyChangeListener;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-7-10 上午09:19:36
 */
public abstract class MiddleChartComponent extends JComponent {

	public abstract void populate(BaseChartCollection cc);

	public abstract BaseChartCollection update();
	
	public abstract void reset();
	
    public abstract BaseChart getEditingChart();
    
    public abstract void addStopEditingListener(PropertyChangeListener list);
}
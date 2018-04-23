package com.fr.design.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.JWizardPanel;

/**
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-4-23 下午03:07:52
* 类说明: 图表向导界面
 */
public abstract class ChartWizardPane extends JWizardPanel {

	public ChartWizardPane() {
		super();
	}
	
	public abstract void update(ChartCollection cc);
	
	public abstract void populate(ChartCollection cc);
}
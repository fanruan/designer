package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Bar3DPlot;
import com.fr.chart.chartattr.BarPlot;
import com.fr.chart.chartattr.Chart;

/**
 * 抽象的 柱形 条形选择类型 布局界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-25 下午04:55:05
 */
public abstract class AbstractBarPane extends AbstractDeprecatedChartTypePane {

    protected static final int COLOMN_CHART = 0;
	protected static final int STACK_COLOMN_CHART = 1;
	protected static final int PERCENT_STACK_COLOMN_CHART = 2;
	protected static final int THREE_D_COLOMN_CHART = 3;
	protected static final int THREE_D_COLOMN_HORIZON_DRAW_CHART = 4;
	protected static final int THREE_D_STACK_COLOMN_CHART = 5;
	protected static final int THREE_D_PERCENT_STACK_COLOMN_CHART = 6;

	@Override
	public void populateBean(Chart chart) {
		super.populateBean(chart);
		BarPlot barPlot = (BarPlot)chart.getPlot();
		if(barPlot.isStacked()) {
			if(barPlot.getyAxis().isPercentage()) {
				if(barPlot instanceof Bar3DPlot){
					typeDemo.get(THREE_D_PERCENT_STACK_COLOMN_CHART).isPressing = true;
				} else {
					typeDemo.get(PERCENT_STACK_COLOMN_CHART).isPressing = true;
				}
			} else {
				if(barPlot instanceof Bar3DPlot){
					typeDemo.get(THREE_D_STACK_COLOMN_CHART).isPressing = true;
				} else {
					typeDemo.get(STACK_COLOMN_CHART).isPressing = true;
				}
			}
		} else {
			if(barPlot instanceof Bar3DPlot) {
				if(((Bar3DPlot) barPlot).isHorizontalDrawBar()) {
					typeDemo.get(THREE_D_COLOMN_HORIZON_DRAW_CHART).isPressing = true;
				} else {
					typeDemo.get(THREE_D_COLOMN_CHART).isPressing = true;
				}
			} else {
				typeDemo.get(COLOMN_CHART).isPressing = true;
			}
		}
		
		checkDemosBackground();
	}
}
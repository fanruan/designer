package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.GanttIndependentChart;
import com.fr.design.i18n.Toolkit;


/**
 * 甘特图 属性表 选择类型 布局界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-26 上午10:38:18
 */
public class GanttPlotPane extends AbstractDeprecatedChartTypePane {
	private static final long serialVersionUID = -601566194238908115L;

	private static final int GANTT = 0;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/GanttPlot/type/0.png",
        };
    }

	@Override
	protected String[] getTypeTipName() {
		return new String[]{
				Toolkit.i18nText("Fine-Design_Chart_Type_Gantt")
		};
    }

	@Override
	protected String getPlotTypeID() {
		return ChartConstants.GANTT_CHART;
	}

    protected String[] getTypeLayoutPath() {
        return new String[0];
    }

	@Override
	protected String[] getTypeLayoutTipName() {
		return new String[0];
	}

	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Toolkit.i18nText("Fine-Design_Chart_Type_Gantt");
	}

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(chart.getPlot() != null && chart.getPlot().getPlotStyle() != ChartConstants.STYLE_NONE){
            resetChart(chart);
        }
        changePlotWithClone(chart, GanttIndependentChart.createGanttChart().getPlot());
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(0).isPressing = true; 
		
		checkDemosBackground();
	}

	public Chart getDefaultChart() {
        return GanttIndependentChart.ganttChartTypes[0];
    }
}
package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.BubbleIndependentChart;
import com.fr.design.i18n.Toolkit;


/**
 * 气泡图 属性表 选择类型 布局界面.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-25 下午06:56:47
 */
public class BubblePlotPane extends AbstractDeprecatedChartTypePane {
    private static final long serialVersionUID = -601566194238908115L;

	private static final int BUBBLE_CHART = 0;

	@Override
	protected String[] getTypeIconPath() {
		return new String[]{"/com/fr/design/images/chart/BubblePlot/type/0.png",};
	}

	@Override
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
		return Toolkit.i18nText("Fine-Design_Chart_Bubble_Chart_OLD");
	}

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }

		BubblePlot plot = new BubblePlot();
		chart.switchPlot(plot);
	}

    @Override
	protected String getPlotTypeID() {
		return ChartConstants.BUBBLE_CHART;
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(0).isPressing = true;

		checkDemosBackground();
	}

	public Chart getDefaultChart() {
        return BubbleIndependentChart.bubbleChartTypes[0];
    }
}
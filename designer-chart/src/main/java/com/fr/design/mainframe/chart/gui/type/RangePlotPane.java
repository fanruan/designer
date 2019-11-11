package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.RangePlot;
import com.fr.chart.charttypes.RangeIndependentChart;
import com.fr.design.i18n.Toolkit;


/**
 * 全距图 属性表 选择类型 布局 界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-26 上午10:43:50
 */
public class RangePlotPane extends AbstractDeprecatedChartTypePane {
	private static final long serialVersionUID = -601566194238908115L;

	private static final int RANGE = 0;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/RangePlot/type/0.png",
        };
    }

	@Override
	protected String getPlotTypeID() {
		return ChartConstants.RANGE_CHART;
	}

    protected String[] getTypeLayoutPath() {
        return new String[]{
        };
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
		return Toolkit.i18nText("Fine-Design_Chart_Type_Range");
	}

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }

		RangePlot plot = new RangePlot();
		chart.switchPlot(plot);
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(RANGE).isPressing = true;

		checkDemosBackground();
	}

	public Chart getDefaultChart() {
        return RangeIndependentChart.rangeChartTypes[0];
    }
}
package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.LinePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.LineIndependentChart;
import com.fr.design.i18n.Toolkit;
import com.fr.log.FineLoggerFactory;


public class LinePlotPane extends AbstractDeprecatedChartTypePane {

	private static final int LINE_CHART = 0;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/LinePlot/type/0.png"
        };
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
	 * 更新折线的类型选择界面
	 */
	public void populateBean(Chart chart) {
		super.populateBean(chart);
		typeDemo.get(LINE_CHART).isPressing = true;
		
		checkDemosBackground();
	}

	/**
	 * 保存界面属性: 折线的选择类型界面
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }

		Chart[] cs = LineIndependentChart.lineChartTypes;
		LinePlot plot;
		if (cs.length > 0) {
			try {
				plot = (LinePlot)cs[0].getPlot().clone();
			} catch (Exception e) {
				plot = new LinePlot();
			}
		} else {
			plot = new LinePlot();
		}
		
		try {
			chart.switchPlot((Plot)plot.clone());
		} catch (CloneNotSupportedException e) {
			FineLoggerFactory.getLogger().error("Error In LineChart");
			chart.switchPlot(new LinePlot());
		}
	}

	/**
	 * 界面标题
     * @return 界面标题
	 */
	@Override
	protected String getPlotTypeID() {
		return ChartConstants.LINE_CHART;
	}

	public String title4PopupWindow() {
		return Toolkit.i18nText("Fine-Design_Chart_Type_Line");
	}

	public Chart getDefaultChart() {
        return LineIndependentChart.lineChartTypes[0];
    }

}
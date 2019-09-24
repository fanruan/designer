package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.RadarIndependentChart;
import com.fr.design.i18n.Toolkit;


/**
 * 雷达图 属性表 选择类型 布局界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-26 上午09:55:01
 */
public class RadarPlotPane extends AbstractDeprecatedChartTypePane {
	private static final long serialVersionUID = -601566194238908115L;

	private static final int RADAR = 0;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/RadarPlot/type/0.png",
        };
    }

	@Override
	protected String[] getTypeTipName() {
		return new String[]{
		        Toolkit.i18nText("Fine-Design_Chart_Type_Radar")
		};
    }

	@Override
	protected String getPlotTypeID() {
		return ChartConstants.RADAR_CHART;
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
	 * 界面弹出标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Toolkit.i18nText("Fine-Design_Chart_Type_Radar");
	}

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }
		chart.switchPlot(RadarIndependentChart.createRadarChart().getPlot());
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(RADAR).isPressing = true;

		checkDemosBackground();
	}

	public Chart getDefaultChart() {
        return RadarIndependentChart.radarChartTypes[0];
    }
}
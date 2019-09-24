package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.CustomIndependentChart;
import com.fr.design.i18n.Toolkit;


/**
 * 组合图 属性表 图表类型 界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-21 下午06:17:40
 */
public class CustomPlotPane extends AbstractDeprecatedChartTypePane {

	@Override
	protected String getPlotTypeID() {
		return ChartConstants.CUSTOM_CHART;
	}

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/CustomPlot/type/0.png",
        };
    }

	@Override
	protected String[] getTypeTipName() {
		return new String[]{
				Toolkit.i18nText("Fine-Design_Chart_Type_Combine")
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
	 * 界面 标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Toolkit.i18nText("Fine-Design_Chart_Type_Combine");
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		super.populateBean(chart);
		
		typeDemo.get(0).isPressing = true;
		
		checkDemosBackground();
	}
	
	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        changePlotWithClone(chart, CustomIndependentChart.createCustomChart().getPlot());
	}

	public Chart getDefaultChart() {
        return CustomIndependentChart.combChartTypes[0];
    }
}
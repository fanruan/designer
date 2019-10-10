package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.charttypes.XYScatterIndependentChart;
import com.fr.design.i18n.Toolkit;
import com.fr.stable.Constants;

/**
 * 散点图 属性表 选择类型 布局界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-25 下午08:33:55
 */
public class XYScatterPlotPane extends AbstractDeprecatedChartTypePane {
	private static final long serialVersionUID = -601566194238908115L;

	private static final int XYSCATTER_CHART = 0;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/XYScatterPlot/type/0.png",
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
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Toolkit.i18nText("Fine-Design_Chart_Type_XYScatter");
	}

	/**
	 * 保存界面内容
	 */
	public void updateBean(Chart chart) {

        if(needsResetChart(chart)){
            resetChart(chart);
        }

		XYScatterPlot plot = new XYScatterPlot();
		chart.switchPlot(plot);

		ConditionAttr conditionAttr = plot.getConditionCollection().getDefaultAttr();
		 AttrLineStyle lineStyle = (AttrLineStyle) conditionAttr.getConditionInType(AttrLineStyle.XML_TAG);
		 if (lineStyle != null) {
			 conditionAttr.remove(lineStyle);
		 }
		 conditionAttr.addDataSeriesCondition(new AttrLineStyle(Constants.LINE_NONE));
	}

    @Override
	protected String getPlotTypeID() {
		return ChartConstants.SCATTER_CHART;
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(XYSCATTER_CHART).isPressing = true;
		checkDemosBackground();
	}

	public Chart getDefaultChart() {
        return XYScatterIndependentChart.XYScatterChartTypes[0];
    }
}

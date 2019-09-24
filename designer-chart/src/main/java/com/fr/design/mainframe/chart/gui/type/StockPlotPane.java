package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.StockIndependentChart;
import com.fr.design.i18n.Toolkit;


/**
 * 股价图 属性表 选择类型 布局 界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-26 上午10:52:36
 */
public class StockPlotPane extends AbstractDeprecatedChartTypePane {

	private static final int STOCK = 0;


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/StockPlot/type/0.png",
        };
    }

	@Override
	protected String[] getTypeTipName() {
		return new String[]{
				Toolkit.i18nText("Fine-Design_Chart_Type_Stock")
		};
    }

	@Override
	protected String getPlotTypeID() {
		return ChartConstants.STOCK_CHART;
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
		return Toolkit.i18nText("Fine-Design_Chart_Type_Stock");
	}

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }
        changePlotWithClone(chart, StockIndependentChart.createStockChart().getPlot());
	}

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
		typeDemo.get(STOCK).isPressing = true; 
		
		checkDemosBackground();
	}

	public Chart getDefaultChart() {
        return StockIndependentChart.stockChartTypes[0];
    }
}
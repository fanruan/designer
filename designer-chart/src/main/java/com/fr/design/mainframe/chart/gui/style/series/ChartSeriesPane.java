package com.fr.design.mainframe.chart.gui.style.series;


import com.fr.chart.chartattr.Chart;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.van.chart.designer.AbstractVanChartScrollPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * 属性表, 图表样式 -- 系列 界面. 通过initDiffer 加载不同Plot 不同的Pane.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-5 上午10:31:50
 */
public class ChartSeriesPane extends AbstractVanChartScrollPane<Chart> {

	protected AbstractPlotSeriesPane seriesStyleContentPane;
	protected Chart chart;

	protected ChartStylePane parent;


	public ChartSeriesPane(ChartStylePane parent) {
		super();
		this.parent = parent;
	}
	/**
	 * 界面标题
	 * @return 系列
	 */
	@Override
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_SERIES_TITLE;
	}

	@Override
	protected JPanel createContentPane() {
		JPanel contentPane = new JPanel(new BorderLayout());
		if(chart == null) {
			return contentPane;
		} 
		initDifferentPlotPane();
		if(seriesStyleContentPane != null) {
			contentPane.add(seriesStyleContentPane, BorderLayout.CENTER);
		}
		return contentPane;
	}

	/**
	 * 保存界面属性
	 */
	@Override
	public void updateBean(Chart chart) {
		if(chart == null) {
			return;
		}
		if(seriesStyleContentPane != null) {
			seriesStyleContentPane.setCurrentChart(chart);
			seriesStyleContentPane.updateBean(chart.getPlot());
			//系列埋点
			ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.SERIES, chart.getPlot().getBuryingPointSeriesConfig());
		}
	}

	/**
	 * 更新界面
	 */
	@Override
	public void populateBean(Chart chart) {
		this.chart = chart;
		if(seriesStyleContentPane == null) {
			this.remove(leftcontentPane);
			layoutContentPane();
			parent.initAllListeners();
		}
		if(seriesStyleContentPane != null) {
			seriesStyleContentPane.setCurrentChart(chart);
			seriesStyleContentPane.populateBean(chart.getPlot());
		}
	}
	
	/**
	 * 初始化不同的Plot系列界面.
	 */
	public void initDifferentPlotPane() {
        seriesStyleContentPane =  (AbstractPlotSeriesPane)ChartTypeInterfaceManager.getInstance().getPlotSeriesPane(parent, this.chart.getPlot());
	}

}
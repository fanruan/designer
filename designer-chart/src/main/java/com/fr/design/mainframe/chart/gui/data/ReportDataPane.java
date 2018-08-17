package com.fr.design.mainframe.chart.gui.data;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.ReportDataDefinition;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;


import java.awt.*;

public class ReportDataPane extends FurtherBasicBeanPane<ChartCollection>{

	private AbstractReportDataContentPane contentPane;
	private ChartDataPane parent;
	
	public ReportDataPane(ChartDataPane parent) {
		this.parent = parent;
	}
	
	protected AbstractReportDataContentPane getContentPane(Chart chart) {
		if(chart == null) {
			return null;
		}
		
		Plot plot = chart.getPlot();
		
		return ChartTypeInterfaceManager.getInstance().getReportDataSourcePane(plot, parent);
	}

    /**
     * 单元格数据
     * @return 界面的标题
     */
	public String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Cell_Data");
	}

    /**
     * 界面是否接受这个对象
     * @param ob 需要判断的对象
     * @return 界面是否接受对象
     */
	public boolean accept(Object ob) {
		return ob instanceof ChartCollection && ((ChartCollection)ob).getSelectedChart().getFilterDefinition() instanceof ReportDataDefinition;
	}

    /**
     * 界面重置
     */
	public void reset() {
		clear();
	}

    /**
     * 刷新contentPane
     * @param collection 图表属性的集合
     */
	public void refreshContentPane(ChartCollection collection) {
		this.removeAll();
		
		this.setLayout(new BorderLayout());
		contentPane = getContentPane(collection.getSelectedChart());
		if(contentPane != null) {
			this.add(contentPane, BorderLayout.CENTER);
		}
	}
	
	/**
	 * 检查界面是否可用.
	 */
	public void checkBoxUse() {
		if(contentPane != null) {
			contentPane.checkBoxUse();
		}
	}

	@Override
	public void populateBean(ChartCollection collection) {
		if(collection == null) {
			return;
		}
		if(contentPane != null) {
			contentPane.populateBean(collection);
		}
	}
	
	public void updateBean(ChartCollection collection) {
		if(contentPane != null) {
			contentPane.updateBean(collection);
		}
	}

	@Override
	public ChartCollection updateBean() {
		return null;
	}

	public void clear() {
		this.removeAll();
	}
}
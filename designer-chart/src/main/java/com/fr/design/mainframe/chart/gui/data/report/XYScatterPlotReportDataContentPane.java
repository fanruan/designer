package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartdata.ScatterReportDefinition;
import com.fr.chart.chartdata.ScatterSeriesValue;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;

import java.util.ArrayList;
import java.util.List;

/**
 * 散点图 属性表 单元格数据源.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-19 下午03:42:37
 */
public class XYScatterPlotReportDataContentPane extends AbstractReportDataContentPane {
	
	private ChartDataFilterPane filterPane;
	
	public XYScatterPlotReportDataContentPane(ChartDataPane parent) {
		initEveryPane();
		
		this.add(new BoldFontTextLabel(Inter.getLocText("Data_Filter")), "0,4,2,4");
		this.add(filterPane = new ChartDataFilterPane(new XYScatterPlot(), parent), "0,6,2,4");
	}
	
	protected String[] columnNames() {
		return new String[]{
				Inter.getLocText("Series_Name"),
				Inter.getLocText("Chart_Scatter") + "x",
				Inter.getLocText("Chart_Scatter") + "y"
		};
	}
	
	public void populateBean(ChartCollection collection) {
		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		if(definition instanceof ScatterReportDefinition) {
			ScatterReportDefinition scatter = (ScatterReportDefinition)definition;
			
			List list = populateSeriesEntryList(scatter);
			if(list != null && !list.isEmpty()) {
				seriesPane.populateBean(list);
			}
		}
		
		filterPane.populateBean(collection);
	}
	
	private List populateSeriesEntryList(ScatterReportDefinition seriesList) {
		List list = new ArrayList();
		for(int i = 0; i < seriesList.size(); i++) {
			ScatterSeriesValue seriesEntry = (ScatterSeriesValue)seriesList.get(i);
			Object[] nameAndValue = new Object[3];
			nameAndValue[0] = seriesEntry.getScatterSeriesName();
			nameAndValue[1] = seriesEntry.getScatterSeriesX();
			nameAndValue[2] = seriesEntry.getScatterSeriesY();
			list.add(nameAndValue);
		}
		return list;
    }
	
	/**
	 * 保存散点 单元格数据内容到ChartCollection.
	 */
	public void updateBean(ChartCollection collection) {
		ScatterReportDefinition reportDataDefinition = new ScatterReportDefinition();
		collection.getSelectedChart().setFilterDefinition(reportDataDefinition);
		
		updateSeriesEntryList(reportDataDefinition, seriesPane.updateBean());
		
		filterPane.updateBean(collection);
	}
	
	private void updateSeriesEntryList(ScatterReportDefinition list, List<Object[]> valueList) {
		for (int i = 0; i < valueList.size(); i++) {
			Object[] rowValueList = valueList.get(i);

			ScatterSeriesValue seriesEntry = new ScatterSeriesValue();
			
			seriesEntry.setScatterSeriesName(canBeFormula(rowValueList[0]));
			seriesEntry.setScatterSeriesX(canBeFormula(rowValueList[1]));
			seriesEntry.setScatterSeriesY(canBeFormula(rowValueList[2]));
			list.add(seriesEntry);
		}
    }
}
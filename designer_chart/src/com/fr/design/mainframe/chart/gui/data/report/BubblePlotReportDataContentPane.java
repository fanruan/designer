package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.BubbleReportDefinition;
import com.fr.chart.chartdata.BubbleSeriesValue;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * 气泡图 属性表 单元格数据源界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-19 下午03:47:56
 */
public class BubblePlotReportDataContentPane extends AbstractReportDataContentPane {
	private static final int BUBBLE = 4;
	private ChartDataFilterPane filterPane;
	
	public BubblePlotReportDataContentPane(ChartDataPane parent) {
		initEveryPane();
		filterPane = new ChartDataFilterPane(new BubblePlot(), parent);
		JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("FR-Chart-Data_Filter"),filterPane);
		panel.setBorder(getSidesBorder());
		filterPane.setBorder(getFilterPaneBorder());
		this.add(panel, "0,6,2,4");
	}
	
	@Override
	protected String[] columnNames() {
		return  new String[]{
			Inter.getLocText("Bubble-Series_Name"),
			Inter.getLocText("ChartF-X_Axis"),
			Inter.getLocText("ChartF-Y_Axis"),
			Inter.getLocText("FRFont-Size")
		};
	}
	
	public void checkBoxUse() {
	}

	/**
	 * 更新气泡图 单元格界面内容
	 */
	public void populateBean(ChartCollection collection) {
		if(collection != null) {
			TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
			if(definition instanceof BubbleReportDefinition) {
				seriesPane.populateBean(populateSeriesEntryList((BubbleReportDefinition)definition));
			}
		}
		filterPane.populateBean(collection);
	}
	
	private List populateSeriesEntryList(BubbleReportDefinition seriesList) {
		List array = new ArrayList();
		for(int i = 0; i < seriesList.size(); i++) {
			BubbleSeriesValue seriesEntry = (BubbleSeriesValue)seriesList.get(i);
			Object[] nameAndValue = new Object[BUBBLE];
			nameAndValue[0] = seriesEntry.getBubbleSereisName();
			nameAndValue[1] = seriesEntry.getBubbleSeriesX();
			nameAndValue[2] = seriesEntry.getBubbleSeriesY();
			nameAndValue[3] = seriesEntry.getBubbleSeriesSize();
			array.add(nameAndValue);
		}
		return array;
    }
	
	/**
	 * 保存气泡图 单元格界面内容到collection
	 */
	public void updateBean(ChartCollection collection) {
		if(collection != null) {
			BubbleReportDefinition bubbleReport = new BubbleReportDefinition();
			collection.getSelectedChart().setFilterDefinition(bubbleReport);
			
			updateSeriesEntryList(bubbleReport, seriesPane.updateBean());
			filterPane.updateBean(collection);
		}
	}
	
	private void updateSeriesEntryList(BubbleReportDefinition list, List<Object[]> valueList) {
		for (int i = 0; i < valueList.size(); i++) {
			Object[] rowValueList = valueList.get(i);

			BubbleSeriesValue seriesEntry = new BubbleSeriesValue();
			
			seriesEntry.setBubbleSeriesName(canBeFormula(rowValueList[0]));
			seriesEntry.setBubbleSeriesX(canBeFormula(rowValueList[1]));
			seriesEntry.setBubbleSeriesY(canBeFormula(rowValueList[2]));
			seriesEntry.setBubbleSeriesSize(canBeFormula(rowValueList[3]));
			list.add(seriesEntry);
		}
    }
}
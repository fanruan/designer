package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.PiePlot;
import com.fr.chart.chartdata.NormalReportDataDefinition;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;

import java.util.List;

/**
 * 饼图 单元格数据 属性表 界面
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-19 上午10:42:27
 */
public class PiePlotReportDataContentPane extends AbstractReportDataContentPane {
	
	private ChartDataFilterPane filterPane;

	public PiePlotReportDataContentPane(ChartDataPane parent) {
		initEveryPane();
		
		this.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Data_Filter")), "0,4,2,4");
		this.add(filterPane = new ChartDataFilterPane(new PiePlot(), parent), "0,6,2,4");
	}
	
	@Override
	protected String[] columnNames() {
		return new String[]{
				Inter.getLocText(new String[]{"Chart_Legend(Series)", "WF-Name"}),
				Inter.getLocText(new String[]{"Chart_Legend(Series)", "Values"})
		};
	}
	

	public void populateBean(ChartCollection collection) {
		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		if(definition instanceof NormalReportDataDefinition) {
			NormalReportDataDefinition reportDefinition = (NormalReportDataDefinition)definition;
			List list = getEntryList(reportDefinition);
			if(!list.isEmpty()) {
				populateList(list);
			}
		}
		
		filterPane.populateBean(collection);
	}
	
	public void updateBean(ChartCollection collection) {
		collection.getSelectedChart().setFilterDefinition(new NormalReportDataDefinition());
		
		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		if(definition instanceof NormalReportDataDefinition) {
			NormalReportDataDefinition reportDefinition = (NormalReportDataDefinition)definition;
			reportDefinition.setCategoryName("");
			
			List list = updateList();
			for(int i = 0; i < list.size(); i++) {
				Object[] value = (Object[])list.get(i);
				SeriesDefinition sd = new SeriesDefinition();
				
				sd.setSeriesName(canBeFormula(value[0]));
				sd.setValue(canBeFormula(value[1]));
				reportDefinition.add(sd);
			}
		}
		
		filterPane.updateBean(collection);
	}
}
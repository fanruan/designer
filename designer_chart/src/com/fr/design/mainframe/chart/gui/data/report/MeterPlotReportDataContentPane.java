package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.MeterPlot;
import com.fr.chart.chartdata.MeterReportDefinition;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * 仪表盘 属性表 单元格数据界面
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-21 下午02:47:57
 */
public class MeterPlotReportDataContentPane extends AbstractReportDataContentPane {
	
	private static final String CATENAME = Inter.getLocText(new String[]{"ChartF-Meter", "StyleFormat-Category", "WF-Name"});
	private static final String NVALUE = Inter.getLocText("Needle Value");
	
	private TinyFormulaPane singCatePane;
	private TinyFormulaPane singValuePane;
	private ChartDataFilterPane filterPane;
	
	public MeterPlotReportDataContentPane(ChartDataPane parent) {
//		initEveryPane();
//		
//		List list = new ArrayList();
//		list.add(new Object[] { singCateName, "" });
//		list.add(new Object[] { singNeedleValue, "" });
//		seriesPane.populateBean(list);
//		
//		seriesPane.noAddUse();
		
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = { p, p, p};
		
		Component[][] components = new Component[][]{
				new Component[]{new UILabel(CATENAME), singCatePane = new TinyFormulaPane()},
				new Component[]{new UILabel(NVALUE), singValuePane = new TinyFormulaPane()},
				new Component[]{null, null}
		};
		
		JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
		
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.NORTH);
		
		double[] cs = {p};
		components = new Component[][]{
				new Component[]{new JSeparator()},
				new Component[]{new BoldFontTextLabel(Inter.getLocText("Data_Filter"))},
				new Component[]{filterPane = new ChartDataFilterPane(new MeterPlot(), parent)},
		};
		JPanel pane = TableLayoutHelper.createTableLayoutPane(components, rowSize, cs);
		
		this.add(pane, BorderLayout.CENTER);
	}
	
	public void populateBean(ChartCollection collection) {
		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		if(definition instanceof MeterReportDefinition) {
			MeterReportDefinition meterDefinition = (MeterReportDefinition)definition;
			
			if (meterDefinition.getName() != null) {
				singCatePane.getUITextField().setText(meterDefinition.getName().toString());
			}
			if (meterDefinition.getValue() != null) {
				singValuePane.getUITextField().setText(meterDefinition.getValue().toString());
			}
		}
		
		filterPane.populateBean(collection);
	}
	
	public void updateBean(ChartCollection collection) {
		
		if (collection != null) {
			MeterReportDefinition meterDefinition = new MeterReportDefinition();
			
			meterDefinition.setName(canBeFormula(singCatePane.getUITextField().getText()));
			meterDefinition.setValue(canBeFormula(singValuePane.getUITextField().getText()));
			
			collection.getSelectedChart().setFilterDefinition(meterDefinition);
			
			filterPane.updateBean(collection);
		}
	}
	
	@Override
	protected String[] columnNames() {
		return new String[]{"", ""};
	}

}
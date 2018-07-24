package com.fr.design.mainframe.chart.gui.style.datasheet;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.DataSheet;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartAxisPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;

import com.fr.van.chart.designer.component.format.FormatPaneWithNormalType;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChartDatasheetPane extends BasicScrollPane<Chart>{
	private static final long serialVersionUID = -4854070113748783014L;
	private UICheckBox isDatasheetVisable;
	private ChartTextAttrPane textAttrPane;
	private FormatPane formatPane;
	private JPanel datasheetPane;
	private ChartAxisPane axisPane; // 绑定ChartAxisPane是否灰化
	
	private class ContentPane extends JPanel{
		private static final long serialVersionUID = 163052991494925562L;

		public ContentPane() {
			initComponents();
		}
		
		private void initComponents() {
			double p = TableLayout.PREFERRED;
			double f = TableLayout.FILL;
			double[] columnSize = {LayoutConstants.CHART_ATTR_TOMARGIN,f};
			double[] rowSize = { p, p, p, p};
			isDatasheetVisable = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Chart_Show_Data_Sheet"));
			textAttrPane = new ChartTextAttrPane();
			formatPane = new FormatPaneWithNormalType();
            Component[][] components = new Component[][]{
                    new Component[]{null,textAttrPane},
                    new Component[]{new JSeparator(),null},
                    new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Data_Type")), null},
                    new Component[]{null,formatPane},
            };

            datasheetPane = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
            
            double row[] = {p, p};
            double col[] = {f};
            JPanel panel = TableLayoutHelper.createTableLayoutPane(new Component[][]{
            					new Component[]{isDatasheetVisable}, new Component[]{datasheetPane},}, row, col);
            this.setLayout(new BorderLayout());
            this.add(panel,BorderLayout.CENTER);
            
            isDatasheetVisable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					checkAxisPaneUse();
				}
			});
            
            isDatasheetVisable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					checkBoxUse();
				}
			});
		}
	}
	
	/**
	 * 绑定坐标轴界面
	 * @param axisPane 坐标轴界面
	 */
	public void useWithAxis(ChartAxisPane axisPane) {
		this.axisPane = axisPane;
	}
	
	/**
	 * 检查联动的坐标轴界面
	 */
	public void checkAxisPaneUse() {
		if(axisPane != null) {
			axisPane.checkUseWithDataSheet(!isDatasheetVisable.isSelected());
		}
	}
	
	// 检查内部box使用
	private void checkBoxUse() {
		isDatasheetVisable.setEnabled(true);
		datasheetPane.setVisible(isDatasheetVisable.isSelected());
	}

	@Override
	/**
	 * 标题
	 * @return 返回标题
	 */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_DATA_TITLE;
	}
	
	@Override
	protected JPanel createContentPane() {
		return new ContentPane();
	}

	@Override
	public void updateBean(Chart chart) {
		if(chart == null) {
			return;
		}
		Plot plot = chart.getPlot();
		if(plot == null) {
			return;
		}
		DataSheet dataSheet = plot.getDataSheet();
		if (dataSheet == null) {
			dataSheet = new DataSheet();
		}
		dataSheet.setVisible(isDatasheetVisable.isSelected());
		dataSheet.setFont(textAttrPane.updateFRFont());
		dataSheet.setFormat(formatPane.update());
	}

	@Override
	public void populateBean(Chart chart) {
		if(chart == null || chart.getPlot() == null) {
			return;
		}
		
		DataSheet dataSheet = chart.getPlot().getDataSheet();
		if (dataSheet != null) {
			isDatasheetVisable.setSelected(dataSheet.isVisible());
			FRFont font = FRContext.getDefaultValues().getFRFont() == null ? FRFont.getInstance() : FRContext.getDefaultValues().getFRFont();
			textAttrPane.populate(dataSheet.getFont() == null ? font : dataSheet.getFont());
			formatPane.populateBean(dataSheet.getFormat());
		}
		
		checkAxisPaneUse();
		checkBoxUse();
		
		if(chart.isUseMoreDate()) {// 多纬坐标轴情况下 , 数据表不可用.
			GUICoreUtils.setEnabled(this, false);
		} 
	}
}
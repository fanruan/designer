package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartdata.ScatterTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.ComparatorUtils;

import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 属性表  数据界面 散点图  数据集数据界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-27 下午04:12:59
 */
public class XYScatterPlotTableDataContentPane extends AbstractTableDataContentPane{
	
	private UIComboBox seriesName;
	private UIComboBox xCombox;
	private UIComboBox yCombox;
	
	private ChartDataFilterPane dataScreeningPane;
	
	public XYScatterPlotTableDataContentPane(ChartDataPane parent) {
		seriesName = new UIComboBox();
		xCombox = new UIComboBox();
		yCombox = new UIComboBox();
		dataScreeningPane = new ChartDataFilterPane(new XYScatterPlot(), parent);

        seriesName.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
		
		seriesName.setPreferredSize(new Dimension(100, 20));
		xCombox.setPreferredSize(new Dimension(100, 20));
		yCombox.setPreferredSize(new Dimension(100, 20));

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p,f};
		double[] rowSize = { p,p,p,p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{new BoldFontTextLabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_Name") + ":"),seriesName},
                new Component[]{new BoldFontTextLabel("  " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Scatter_Name") + "x" + ":"), xCombox},
                new Component[]{new BoldFontTextLabel("  " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Scatter_Name") + "y" + ":"), yCombox},
                new Component[]{new JSeparator(),null},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Data_Filter"))},
                new Component[]{dataScreeningPane,null}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
        
        seriesName.addItemListener(tooltipListener);
        xCombox.addItemListener(tooltipListener);
        yCombox.addItemListener(tooltipListener);
	}
	
	/**
	 * 联动 box是否可用.
     * @param hasUse  是否使用.
	 */
	public void checkBoxUse(boolean hasUse) {
		seriesName.setEnabled(hasUse);
		xCombox.setEnabled(hasUse);
		yCombox.setEnabled(hasUse);
	}
	
	protected void refreshBoxListWithSelectTableData(List columnNameList) {
		refreshBoxItems(seriesName, columnNameList);
        seriesName.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
		refreshBoxItems(xCombox, columnNameList);
		refreshBoxItems(yCombox, columnNameList);
	}

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        clearBoxItems(seriesName);
        seriesName.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
        clearBoxItems(xCombox);
        clearBoxItems(yCombox);
    }

	/**
	 * 保存散点图 的数据集数据界面.
	 */
	public void updateBean(ChartCollection collection) {
		
		ScatterTableDefinition definition = new ScatterTableDefinition();
		collection.getSelectedChart().setFilterDefinition(definition);
		
		Object resultName = seriesName.getSelectedItem();
		Object resultX = xCombox.getSelectedItem();
		Object resultY = yCombox.getSelectedItem();

        if(resultName == null || ArrayUtils.contains(ChartConstants.getNoneKeys(), resultName)) {
            definition.setSeriesName(StringUtils.EMPTY);
        } else {
			definition.setSeriesName(resultName.toString());
        }

		if(resultX != null) {
			definition.setScatterX(resultX.toString());
		}
		
		if(resultY != null) {
			definition.setScatterY(resultY.toString());
		}
		
		dataScreeningPane.updateBean(collection);
	}

	/**
	 * 更新散点图的数据集数据.
	 */
	public void populateBean(ChartCollection collection) {
		TopDefinitionProvider top = collection.getSelectedChart().getFilterDefinition();
		if(top instanceof ScatterTableDefinition) {
			ScatterTableDefinition definition = (ScatterTableDefinition)top;

            if(definition.getSeriesName() == null || ComparatorUtils.equals(StringUtils.EMPTY, definition.getSeriesName())) {
                seriesName.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
            } else {
			    combineCustomEditValue(seriesName, definition.getSeriesName());
            }

			combineCustomEditValue(xCombox, definition.getScatterX());
			combineCustomEditValue(yCombox, definition.getScatterY());
		}
		dataScreeningPane.populateBean(collection);
	}

	/**
	 * 重新布局
	 */
	public void redoLayoutPane(){
		dataScreeningPane.relayoutPane(this.isNeedSummaryCaculateMethod());
	}
}
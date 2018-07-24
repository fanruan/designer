package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.Utils;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.MeterPlot;
import com.fr.chart.chartdata.MeterTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

/**
 * 仪表盘, 属性表, 数据集数据界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-21 下午04:51:50
 */
public class MeterPlotTableDataContentPane extends AbstractTableDataContentPane {
	private static final String METER_NAME = com.fr.design.i18n.Toolkit.i18nText("Chart-Category_Use_Name");
	private static final String METER_VALUE = com.fr.design.i18n.Toolkit.i18nText("Chart-Pointer_Value");
	
	private UIComboBox nameBox;
	private UIComboBox valueBox;
	private ChartDataFilterPane filterPane;
	
	public MeterPlotTableDataContentPane(ChartDataPane parent) {
		this.setLayout(new BorderLayout());
		
		nameBox = new UIComboBox();
		valueBox = new UIComboBox();
		filterPane = new ChartDataFilterPane(new MeterPlot(), parent);

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = {f, COMPONENT_WIDTH};
		double[] rowSize = {p, p};

        Component[][] components = createComponents();

		JPanel jPanel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Data_Filter"),filterPane);
		JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);

		filterPane.setBorder(BorderFactory.createEmptyBorder(0,10,0,15));
		panel.setBorder(BorderFactory.createEmptyBorder(10,24,10,15));
		jPanel.setBorder(BorderFactory.createEmptyBorder(0,12,0,5));

        this.add(getJSeparator(),BorderLayout.NORTH);
		this.add(panel,BorderLayout.CENTER);
		this.add(jPanel,BorderLayout.SOUTH);

        nameBox.addItemListener(tooltipListener);
        valueBox.addItemListener(tooltipListener);
	}

    private Component[][] createComponents() {
        return new Component[][]{
                new Component[]{new BoldFontTextLabel(METER_NAME), getNameComponent()},
                new Component[]{new BoldFontTextLabel(METER_VALUE), valueBox},
        };
    }

    protected void refreshBoxListWithSelectTableData(List list) {
		refreshBoxItems(nameBox, list);
		refreshBoxItems(valueBox, list);
	}

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        clearBoxItems(nameBox);
        clearBoxItems(valueBox);
    }

	/**
	 * 更新仪表盘数据界面
	 */
	public void populateBean(ChartCollection ob) {
		if(ob != null && ob.getSelectedChart().getFilterDefinition() instanceof MeterTableDefinition) {
			MeterTableDefinition meter = (MeterTableDefinition)ob.getSelectedChart().getFilterDefinition();
            
            populateNameComponent(meter);
            
			valueBox.setSelectedItem(meter.getValue());
			filterPane.populateBean(ob);
		}
	}

    protected void populateNameComponent(MeterTableDefinition meter) {
        nameBox.setSelectedItem(meter.getName());
    }

    /**
	 * 保存界面属性.
	 */
	public void updateBean(ChartCollection ob) {
		if(ob != null) {
			MeterTableDefinition meter = getMeterTableDefinition();
			ob.getSelectedChart().setFilterDefinition(meter);
			
            updateNameComponent(meter);

			meter.setValue(Utils.objectToString(valueBox.getSelectedItem()));
			filterPane.updateBean(ob);
		}
	}

    protected MeterTableDefinition getMeterTableDefinition(){
        return new MeterTableDefinition();
    }

    protected void updateNameComponent(MeterTableDefinition meter) {
        meter.setName(Utils.objectToString(nameBox.getSelectedItem()));
    }

    /**
     * 重新布局整个面板
     */
	public void redoLayoutPane(){
		filterPane.relayoutPane(this.isNeedSummaryCaculateMethod());
	}


    protected Component getNameComponent() {
        return nameBox;
    }
}
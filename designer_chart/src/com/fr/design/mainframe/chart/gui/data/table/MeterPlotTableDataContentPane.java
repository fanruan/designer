package com.fr.design.mainframe.chart.gui.data.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

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
import com.fr.general.Inter;

/**
 * 仪表盘, 属性表, 数据集数据界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-21 下午04:51:50
 */
public class MeterPlotTableDataContentPane extends AbstractTableDataContentPane {
	private static final String METER_NAME = Inter.getLocText("Chart-Category_Use_Name") + ":";
	private static final String METER_VALUE = Inter.getLocText("Chart-Pointer_Value") + ":";
	
	private UIComboBox nameBox;
	private UIComboBox valueBox;
	private ChartDataFilterPane filterPane;
	
	public MeterPlotTableDataContentPane(ChartDataPane parent) {
		this.setLayout(new BorderLayout());
		
		nameBox = new UIComboBox();
		valueBox = new UIComboBox();
		filterPane = new ChartDataFilterPane(new MeterPlot(), parent);
		
		nameBox.setPreferredSize(new Dimension(100, 20));
		valueBox.setPreferredSize(new Dimension(100, 20));

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p,f};
		double[] rowSize = { p, p,p,p,p,p,p,p, p};

        Component[][] components = createComponents();

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.add(panel,BorderLayout.CENTER);
        
        nameBox.addItemListener(tooltipListener);
        valueBox.addItemListener(tooltipListener);
	}

    private Component[][] createComponents() {
        return new Component[][]{
                new Component[]{new BoldFontTextLabel(METER_NAME, SwingConstants.RIGHT), getNameComponent()},
                new Component[]{new BoldFontTextLabel(METER_VALUE, SwingConstants.RIGHT), valueBox},
                new Component[]{new JSeparator(), null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Chart-Data_Filter"))},
                new Component[]{filterPane, null}
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
package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.PiePlot;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import java.awt.*;
import java.util.List;

public class PiePlotTableDataContentPane extends AbstractTableDataContentPane{
	private SeriesTypeUseComboxPane typeChoosePane;
	
	public PiePlotTableDataContentPane(ChartDataPane parent) {
		typeChoosePane = new SeriesTypeUseComboxPane(parent, new PiePlot());
		this.setLayout(new BorderLayout());
		this.add(getJSeparator(), BorderLayout.NORTH);
		this.add(typeChoosePane, BorderLayout.CENTER);
	}

    /**
     * 判断时候使用typeChoosePane
     * @param hasUse 是否使用
     */
	public void checkBoxUse(boolean hasUse) {
		typeChoosePane.checkUseBox(hasUse);
	}
	
	protected void refreshBoxListWithSelectTableData(List list) {
		typeChoosePane.refreshBoxListWithSelectTableData(list);
	}

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        typeChoosePane.clearAllBoxList();
    }
	
	@Override
	public void updateBean(ChartCollection collection) {
		typeChoosePane.updateBean(collection);
	}

	@Override
	public void populateBean(ChartCollection collection) {
		typeChoosePane.populateBean(collection,this.isNeedSummaryCaculateMethod());
	}

    /**
     * 重新布局整个面板
     */
    public void redoLayoutPane(){
        typeChoosePane.relayoutPane(this.isNeedSummaryCaculateMethod());
    }
}
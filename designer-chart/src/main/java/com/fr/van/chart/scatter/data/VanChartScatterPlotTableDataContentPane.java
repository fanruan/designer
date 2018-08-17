package com.fr.van.chart.scatter.data;

import com.fr.chart.chartdata.BubbleTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.ComparatorUtils;

import com.fr.van.chart.bubble.data.VanChartBubblePlotTableDataContentPane;

import java.awt.Dimension;

public class VanChartScatterPlotTableDataContentPane extends VanChartBubblePlotTableDataContentPane {

    public VanChartScatterPlotTableDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    @Override
    protected void initBubbleSize() {
        bubbleSize = new UIComboBox();
        bubbleSize.setPreferredSize(new Dimension(100, 20));
        bubbleSize.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
    }

    @Override
    protected void refreshBoxListWithSelectTableData(java.util.List list) {
        refreshBoxItems(seriesName, list);
        seriesName.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
        refreshBoxItems(xCombox, list);
        refreshBoxItems(yCombox, list);
        refreshBoxItems(bubbleSize, list);
        bubbleSize.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
    }

    @Override
    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        clearBoxItems(seriesName);
        seriesName.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
        clearBoxItems(xCombox);
        clearBoxItems(yCombox);
        clearBoxItems(bubbleSize);
        bubbleSize.addItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
    }

    @Override
    protected void populateBubbleSize(BubbleTableDefinition definition) {
        if (ComparatorUtils.equals(definition.getBubbleSize(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"))){
            bubbleSize.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_None"));
        }else {
            combineCustomEditValue(bubbleSize, definition.getBubbleSize());
        }
    }
}
/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.TableData;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.TableDataDefinition;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotMoreCateTableDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.Factory4TableDataContentPane;

import javax.swing.*;

/**
 * 数据导入数据设置面板
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 下午2:35
 */
public class ImportSetChartDataPane  extends AbstractChartDataPane4Chart {

    private AbstractTableDataContentPane dataContentPane;

    public ImportSetChartDataPane(final AttributeChangeListener listener, ChartDataPane parent) {
         super(listener,parent);
    }


    /**
     * 更新界面 数据内容
     */
    public void populate(ChartCollection collection) {
        dataContentPane = getContentPane(collection.getSelectedChart().getPlot());
        dataContentPane.setNeedSummaryCaculateMethod(false);
        dataContentPane.redoLayoutPane();
        if (collection != null && collection.getSelectedChart() != null) {
            Chart chart = collection.getSelectedChart();
            TopDefinitionProvider definition = chart.getFilterDefinition();
            if (definition instanceof TableDataDefinition) {
                TableData tableData = ((TableDataDefinition) definition).getTableData();
                if(tableData != null){
                    populateChoosePane(tableData);
                    fireTableDataChange();
                }
                if (dataContentPane != null) {
                    dataContentPane.populateBean(collection);
                }
            }
        }
        this.remove(leftContentPane);
        this.initContentPane();
        this.validate();
        dataSource.addItemListener(dsListener);
        initAllListeners();
        initSelfListener(dataContentPane);
        this.addAttributeChangeListener(attributeChangeListener);
    }

    protected JPanel getDataContentPane(){
        return dataContentPane;
    }


    @Override
    public void update(ChartCollection collection) {
        if (collection != null && collection.getSelectedChart() != null) {
            if (dataContentPane != null) {
                dataContentPane.updateBean(collection);
            }
            TableDataDefinition topDefinition =(TableDataDefinition)collection.getSelectedChart().getFilterDefinition();
            if(topDefinition !=null){
                topDefinition.setTableData(choosePane.getTableData());
            }
        }
    }

    private AbstractTableDataContentPane getContentPane(Plot plot) {
        if (plot == null || plot.isSupportMoreCate()) {
   			return new CategoryPlotMoreCateTableDataContentPane(parentPane){
                public boolean isNeedSummaryCaculateMethod(){
                    return false;
                }
            };
        } else{
            return Factory4TableDataContentPane.createTableDataContenetPaneWithPlotType(plot, parentPane);
        }
    }

    /**
     * 数据集数据改变
     */
    public void fireTableDataChange() {
        if (dataContentPane != null) {
            dataContentPane.onSelectTableData(choosePane.getTableDataWrapper());
        }
    }

    /**
     * 清空数据集的设置
     */
    public void clearTableDataSetting(){
        if(dataContentPane != null){
            dataContentPane.clearAllBoxList();
        }
    }

}
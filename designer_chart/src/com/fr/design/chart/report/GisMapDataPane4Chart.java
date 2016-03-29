package com.fr.design.chart.report;

import com.fr.base.TableData;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.*;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class GisMapDataPane4Chart extends AbstractChartDataPane4Chart {

    private GisMapTableDataContentPane4Chart tablePane = new GisMapTableDataContentPane4Chart();

    public GisMapDataPane4Chart(AttributeChangeListener listener, ChartDataPane parent) {
        super(listener, parent);
    }

    protected JPanel getDataContentPane() {
        return tablePane;
    }

    @Override
    public void populate(ChartCollection collection) {
        tablePane = new GisMapTableDataContentPane4Chart();
        if (collection != null && collection.getSelectedChart() != null) {
            Chart chart = collection.getSelectedChart();
            TopDefinitionProvider definition = chart.getFilterDefinition();
            if (definition instanceof TableDataDefinition) {
                TableData tableData = ((TableDataDefinition) definition).getTableData();
                if (tableData != null) {
                    populateChoosePane(tableData);
                    fireTableDataChange();
                }
            }
            if (definition instanceof GisMapTableDefinition) {
                tablePane.populateBean((GisMapTableDefinition) definition);
            }
        }
        this.remove(leftContentPane);
        this.initContentPane();
        this.validate();
        dataSource.addItemListener(dsListener);
        initAllListeners();
        initSelfListener(tablePane);
        this.addAttributeChangeListener(attributeChangeListener);
    }

    @Override
    public void update(ChartCollection collection) {
        collection.getSelectedChart().setFilterDefinition(tablePane.updateBean());
    }


    /**
     * 数据集数据改变
     */
    public void fireTableDataChange() {
        tablePane.fireTableDataChange(choosePane.getTableDataWrapper());
    }
}
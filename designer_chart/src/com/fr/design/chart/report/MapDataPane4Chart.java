package com.fr.design.chart.report;

import com.fr.base.TableData;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.*;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.AbstractChartDataPane4Chart;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * Date: 14/12/3
 * Time: 下午6:49
 */
public class MapDataPane4Chart extends AbstractChartDataPane4Chart {
    private UIComboBoxPane dataContentPane;
    private MapMoreCubeLayerPane4Chart morePane = new MapMoreCubeLayerPane4Chart();
   	private MapSinglePane4Chart singlePane = new MapSinglePane4Chart();
    private ChartCollection currentCollection;
    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            fireTableDataChange();
        }
    };

    public MapDataPane4Chart(AttributeChangeListener listener, ChartDataPane parent) {
        super(listener, parent);
        dataContentPane = new UIComboBoxPane<Chart>() {
            protected void initLayout() {
                this.setLayout(new BorderLayout(0, 6));
                JPanel northPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
                northPane.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Map_ShowWay") + ":"));
                northPane.add(jcb);
                this.add(northPane, BorderLayout.NORTH);
                this.add(cardPane, BorderLayout.CENTER);
            }
            protected java.util.List<FurtherBasicBeanPane<? extends Chart>> initPaneList() {
                java.util.List list = new ArrayList();
                list.add(singlePane);
                list.add(morePane);
                return list;
            }

            protected void comboBoxItemStateChanged() {
                if(currentCollection == null){
                    return;
                }
                fireTableDataChange();
                morePane.init4PopuMapTree(currentCollection);
           	}


            protected String title4PopupWindow() {
                return Inter.getLocText(new String[]{"Chart-Map", "Data"});
            }
        };
    }


    protected JPanel getDataContentPane(){
        return dataContentPane;
    }

    @Override
    public void populate(ChartCollection collection) {
        currentCollection = collection;
      	morePane.init4PopuMapTree(collection);
        if (collection != null && collection.getSelectedChart() != null) {
            Chart chart = collection.getSelectedChart();
            TopDefinitionProvider definition = chart.getFilterDefinition();
            if (definition instanceof TableDataDefinition) {
                TableData tableData = ((TableDataDefinition) definition).getTableData();
                if(tableData != null){
                    populateChoosePane(tableData);
                    fireTableDataChange();
                }
            }
            if(definition instanceof MapSingleLayerTableDefinition) {
           		singlePane.populateBean(definition);
           	} else if(definition instanceof MapMoreLayerTableDefinition) {
           		morePane.populateBean(collection);
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

    @Override
    public void update(ChartCollection collection) {
        if(dataContentPane.getSelectedIndex() == 0) {
      		collection.getSelectedChart().setFilterDefinition(singlePane.updateBean());
      	} else {
      		morePane.updateBean(collection);
      	}
        currentCollection = collection;
    }

    /**
     * 数据集数据改变
     */
    public void fireTableDataChange() {
        if (dataContentPane == null) {
            return;
        }
        if(dataContentPane.getSelectedIndex() == 0) {
            singlePane.fireTableDataChanged(choosePane.getTableDataWrapper());
      	} else {
            morePane.fireTableDataChanged(choosePane.getTableDataWrapper());
      	}

    }

}
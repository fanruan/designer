/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.mainframe.ChartDesigner;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.general.ComparatorUtils;
import com.fr.js.NameJavaScriptGroup;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

/**
 * 图表设计器 工具栏面板
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-16
 * Time: 下午5:17
 */
public abstract class PlotPane4ToolBar extends JPanel{
    private static final int COM_GAP = 14;

    protected List<ChartDesignerImagePane> typeDemo;

    protected abstract String getTypeIconPath();

    protected abstract List<ChartDesignerImagePane> initDemoList();

    private int selectedIndex = 0;//默认选中第一个
    private ChartDesigner chartDesigner;
    private ChangeListener changeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    };

    public PlotPane4ToolBar(ChartDesigner designer){
        chartDesigner = designer;
        typeDemo = initDemoList();
        this.setLayout(new FlowLayout(FlowLayout.LEFT, COM_GAP, 0));
        for(int i = 0; i < typeDemo.size(); i++) {
            ChartDesignerImagePane tmp = typeDemo.get(i);
            tmp.registeChangeListener(changeListener);
            this.add(tmp);
        }
        this.setSelectedIndex(0);
    }

    /**
     * 清除选中
     */
    public void clearChoose(){
        if(typeDemo == null){
            return;
        }
        for(int i = 0; i < typeDemo.size(); i++) {
            typeDemo.get(i).setSelected(false);
            this.repaint();
        }
    }

    public void setSelectedIndex(int selectedIndex){
        clearChoose();
        this.selectedIndex = selectedIndex;
        typeDemo.get(selectedIndex).setSelected(true);
    }

    public int getSelectedIndex(){
        return this.selectedIndex;
    }

        //子类覆盖
    protected Plot getSelectedClonedPlot(){
        return null;
    }

    /**
     * 切换图表类型
     */
    public void fireChange(){
        ChartCollection chartCollection = (ChartCollection)chartDesigner.getTarget().getChartCollection();
        Chart chart =chartCollection.getSelectedChart();
        chart.switchPlot(getSelectedClonedPlot());
        resetChart(chart);
        chartDesigner.clearToolBarStyleChoose();
        chartDesigner.fireTargetModified();
        ChartDesignEditPane.getInstance().populateSelectedTabPane();
    }

    protected void resetChart(Chart chart){
        chart.setBorderStyle(Constants.LINE_NONE);
        chart.setBorderColor(new Color(150, 150, 150));
        chart.setBackground(null);
    }

    public Plot setSelectedClonedPlotWithCondition(Plot oldPlot){
        Plot newPlot = getSelectedClonedPlot();
        if(oldPlot != null && ComparatorUtils.equals(newPlot.getClass(), oldPlot.getClass())){
            if(oldPlot.getHotHyperLink() != null){
                NameJavaScriptGroup hotHyper = oldPlot.getHotHyperLink();
                try {
                    newPlot.setHotHyperLink((NameJavaScriptGroup)hotHyper.clone());
                } catch (CloneNotSupportedException e) {
                    FRContext.getLogger().error("Error in Hyperlink, Please Check it.", e);
                }
            }
            newPlot.setConditionCollection(oldPlot.getConditionCollection());
            newPlot.setSeriesDragEnable(oldPlot.isSeriesDragEnable());
            if(newPlot.isSupportZoomCategoryAxis() && newPlot.getxAxis() != null) {
                newPlot.getxAxis().setZoom(oldPlot.getxAxis().isZoom());
            }
            if(newPlot.isSupportTooltipInInteractivePane()) {
                newPlot.setHotTooltipStyle(oldPlot.getHotTooltipStyle());
            }

            if(newPlot.isSupportAutoRefresh()) {
                newPlot.setAutoRefreshPerSecond(oldPlot.getAutoRefreshPerSecond());
            }
        }
        return newPlot;
    }
}
package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.ChartCollection;

/**
 * Created by mengao on 2017/5/3.
 */
public abstract class ThirdChartEditPane extends ChartEditPane {

    protected void createTabsPane() {}

    public String getSelectedTabName() {
        return paneList.get(1).title4PopupWindow();
    }

    public void populate(ChartCollection collection) {
        populateBean(collection.getSelectedChart());
    }

    protected void dealWithStyleChange(){
        populateBean(collection.getSelectedChart());
    }

    /**
     * 图表设计器，显示选中的面板
     */
    public void populateSelectedTabPane() {
        populateBean(collection.getSelectedChart());
    }

    /**
     * 数据集改变的事件监听
     */
    public void registerDSChangeListener() {
    }

    @Override
    protected String title4PopupWindow() {
        return "CustomChart";
    }

    protected abstract void populateBean(Object ob);

    protected abstract void updateBean(Object ob);

}

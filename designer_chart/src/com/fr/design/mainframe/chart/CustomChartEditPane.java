package com.fr.design.mainframe.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.TargetComponentContainer;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/5/3.
 */
public class CustomChartEditPane extends ChartEditPane  {
    //构建主面板
    protected void createTabsPane() {
        Icon[] iconArray = new Icon[paneList.size()];
        card = new CardLayout();
        center = new JPanel(card);


        tabsHeaderIconPane = new UIHeadGroup(iconArray) {
            @Override
            public void tabChanged(int index) {
            }
        };

    }

    AttributeChangeListener listener = new AttributeChangeListener() {
        @Override
        public void attributeChange() {
            chartEditPane.updateBean(collection.getSelectedChart());
        }
    };

    /**
     * 重新构造面板
     * @param currentChart 图表
     */
    public void reLayout(Chart currentChart){
    }

    protected ChartDataPane createChartDataPane(String plotID) {
        ChartDataPane chartDataPane = ChartTypeInterfaceManager.getInstance().getChartDataPane(plotID, listener);
        chartDataPane.setSupportCellData(dataPane4SupportCell.isSupportCellData());
        return chartDataPane;
    }

    protected void addTypePane() {
        paneList.add(typePane);
    }

    protected void setSelectedTab() {
    }

    /**
     * 设置是否支持单元格
     */
    public void setSupportCellData(boolean supportCellData) {

    }

    /**
     * 返回选中的tab名称.
     */
    public String getSelectedTabName() {
        int index = Math.min(tabsHeaderIconPane.getSelectedIndex(), paneList.size() - 1);
        return paneList.get(index).title4PopupWindow();
    }

    /**
     * 添加标题变化监听事件.
     *
     * @param titleChangeListener 标题的ChangeListener
     */
    public void addTitleChangeListener(TitleChangeListener titleChangeListener) {
        this.titleChangeListener = titleChangeListener;
    }

    /**
     * 设置容器.
     */
    public void setContainer(TargetComponentContainer container) {

    }



    public void populate(ChartCollection collection) {
        chartEditPane.populateBean(collection.getSelectedChart());
    }

    protected ChartEditPane getCurrentChartEditPane() {
        return this;
    }


    /**
     * 响应事件.
     */
    public void fire() {

    }

    public int getSelectedChartIndex(Chart chart){
        int index = 0;
        return index;
    }

    //populate的时候看看要不要重构面板
    private boolean checkNeedsReLayout(Chart chart){

        return false;
    }

    /**
     * 当前界面是否是默认的界面
     * @return 是否是默认的界面
     */
    public boolean isDefaultPane(){
        return this.isDefaultPane;
    }

    /**
     * 展开到对应id的pane.
     *
     * @param id 界面的标志.id
     */
    public void gotoPane(String... id) {

    }

    /**
     * 设置选中的id
     *
     * @param id 界面的标志.id
     */
    public void setSelectedIndex(String... id) {

    }

    protected void dealWithStyleChange(){
        chartEditPane.populateBean(collection.getSelectedChart());
    }

    /**
     *主要用于图表设计器，判断样式改变是否来自工具栏的全局样式按钮
     * @param isFromToolBar 是否来自工具栏
     */
    public void styleChange(boolean isFromToolBar){

    }

    /**
     * 图表设计器，显示选中的面板
     */
    public void populateSelectedTabPane() {
        chartEditPane.populateBean(collection.getSelectedChart());
    }

    /**
     * 数据集改变的事件监听
     */
    public void registerDSChangeListener() {

    }
}

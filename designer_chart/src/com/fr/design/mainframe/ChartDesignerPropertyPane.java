/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe;

import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.general.Inter;


import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 下午12:43
 */
public class ChartDesignerPropertyPane extends BaseChartPropertyPane {
    private static ChartDesignerPropertyPane instance;
    private TargetComponentContainer container = new TargetComponentContainer();
    private ChartEditPane chartEditPane;
    private UILabel nameLabel;
    private TitleChangeListener titleListener = new TitleChangeListener() {

        @Override
        public void fireTitleChange(String addName) {
            nameLabel.setText(Inter.getLocText("Chart-Property_Table") + '-' + addName);
        }
    };

    public synchronized static ChartDesignerPropertyPane getInstance() {
        if (instance == null) {
            instance = new ChartDesignerPropertyPane();
        }
        instance.setSureProperty();
        return instance;
    }

    public ChartDesignerPropertyPane() {
        this.setLayout(new BorderLayout());
        this.setBorder(null);

        createNameLabel();
        this.add(nameLabel, BorderLayout.NORTH);
        chartEditPane = ChartDesignEditPane.getInstance();
        this.add(chartEditPane, BorderLayout.CENTER);
    }

    private void createNameLabel() {
        nameLabel = new UILabel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 18);
            }
        };
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public void setSureProperty() {
        chartEditPane.setContainer(container);
        chartEditPane.addTitleChangeListener(titleListener);
        String tabname = chartEditPane.getSelectedTabName();
        nameLabel.setText(Inter.getLocText("Chart-Property_Table") + (tabname != null ? ('-' + chartEditPane.getSelectedTabName()) : ""));
        resetChartEditPane();

    }

    protected void resetChartEditPane() {
        remove(chartEditPane);
        add(chartEditPane, BorderLayout.CENTER);
        validate();
        repaint();
        revalidate();
    }

    @Override
    public void setSupportCellData(boolean supportCellData){

    }

    /**
     * 感觉ChartCollection加载图表属性界面.
     * @param collection  收集图表
     * @param chartDesigner  图表设计
     */
    public void populateChartPropertyPane(BaseChartCollection collection, TargetComponent<?> chartDesigner) {
        if (collection instanceof ChartCollection) {
            this.container.setEPane(chartDesigner);
            chartEditPane.populate((ChartCollection) collection);
        }
    }

    @Override
    public void setWidgetPropertyPane(BaseWidgetPropertyPane pane) {

    }

    /**
     * 刷新
     */
    public void refreshDockingView() {

    }

    @Override
    public String getViewTitle() {
        return null;
    }

    @Override
    public Icon getViewIcon() {
        return null;
    }

    /**
     * 位置
     * @return 位置
     */
    public Location preferredLocation() {
        return null;
    }
}
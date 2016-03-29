/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.ColumnIndependentChart;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.general.FRLogger;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: 下午4:55
 */
public class ChartDesignerUI extends ComponentUI {
    private static final Icon ADD = BaseUtils.readIcon("/com/fr/design/images/add.png");
    private static final Icon DEL = BaseUtils.readIcon("/com/fr/design/images/del.png");
    private static final int ICON_SIZE = 22;
    private static final int H_GAP = 2;
    private static final int V_GAP = 6;
    private Rectangle[] iconLocations;
    private Rectangle add;
    private Rectangle del;
    private UILabel tooltipLabel;
    private int overIndex = -1;//鼠标悬浮上去的图表的INDEX

    // 图表当前的设计器
    private ChartDesigner designer;

    public ChartDesignerUI() {


    }

    /**
     * 加载界面
     *
     * @param c 组件
     */
    public void installUI(JComponent c) {
        designer = (ChartDesigner) c;
    }

    /**
     * 渲染当前的设计界面以及设计辅助状态
     *
     * @param g 画图类
     * @param c 组件
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        ChartCollection chartCollection = (ChartCollection) designer.getTarget().getChartCollection();
        Chart editingChart = chartCollection.getSelectedChart();
        BaseChartGlyph chartGlyph = null;
        if (editingChart != null && editingChart.getPlot() != null) {
            chartGlyph = editingChart.createGlyph(editingChart.defaultChartData());
        }
        int parentWidth = designer.getSize().width;
        int parentHeight = designer.getSize().height;
        int chartWidth = designer.getArea().getCustomWidth();
        int chartHeight = designer.getArea().getCustomHeight();
        Graphics clipg;
        clipg = g.create(-designer.getArea().getHorizontalValue(), -designer.getArea().getVerticalValue(), parentWidth + designer.getArea().getHorizontalValue(), parentHeight + designer.getArea().getVerticalValue());
        clipg = clipg.create(1, 1, designer.getArea().getCustomWidth(), designer.getArea().getCustomHeight());
        g.setColor(Color.white);
        g.fillRect(0, 0, chartWidth, chartHeight);
        chartGlyph.setUseChangeChart(true);
        Image chartImage = chartGlyph.toImage(chartWidth, chartHeight, ScreenResolution.getScreenResolution());
        clipg.drawImage(chartImage, 0, 0, chartWidth, chartHeight, null);
        paintChange(clipg, c);
    }


    //绘制切换的东西
    private void paintChange(Graphics g, JComponent c) {
        int chartWidth = designer.getArea().getCustomWidth();
        ChartCollection collection = (ChartCollection) designer.getTarget().getChartCollection();
        int chartCount = collection.getChartCount();
        iconLocations = new Rectangle[chartCount];
        int startX = chartWidth - V_GAP - ICON_SIZE;
        if (chartCount == 1) {
            //只有一个时，只绘制新增按钮，不绘制删除按钮
            ADD.paintIcon(c, g, startX, H_GAP);
            add = new Rectangle(startX, H_GAP, ICON_SIZE, ICON_SIZE);
            del = null;
        } else {
            DEL.paintIcon(c, g, startX, H_GAP);
            del = new Rectangle(startX, H_GAP, ICON_SIZE, ICON_SIZE);
            startX -= (V_GAP + ICON_SIZE);
            ADD.paintIcon(c, g, startX, H_GAP);
            add = new Rectangle(startX, H_GAP, ICON_SIZE, ICON_SIZE);
        }

        for (int i = chartCount - 1; i >= 0; i--) {
            Plot plot = collection.getChart(i).getPlot();
            if (plot == null) {
                continue;
            }
            if (collection.getSelectedIndex() == i) {
                Icon ploticon = BaseUtils.readIcon(plot.getPlotSmallIconPath() + "_normal.png");
                if (ploticon != null) {
                    startX -= (V_GAP + ICON_SIZE);
                    ploticon.paintIcon(c, g, startX, H_GAP);
                }

            }else if(overIndex == i){
                Icon ploticon = BaseUtils.readIcon(plot.getPlotSmallIconPath() + "_over.png");
                if (ploticon != null) {
                    startX -= (V_GAP + ICON_SIZE);
                    ploticon.paintIcon(c, g, startX, H_GAP);
                }
            } else {
                Icon ploticon = BaseUtils.readIcon(plot.getPlotSmallIconPath() + "_gray.png");
                if (ploticon != null) {
                    startX -= (V_GAP + ICON_SIZE);
                    ploticon.paintIcon(c, g, startX, H_GAP);
                }
            }

            iconLocations[i] = new Rectangle(startX, H_GAP, ICON_SIZE, ICON_SIZE);
        }
    }

    /**
     * 鼠标点击
     *
     * @param e 事件
     */
    public void mouseClicked(MouseEvent e) {
        Point clikPoint = new Point(e.getPoint().x + designer.getArea().getHorizontalValue(), e.getPoint().y + designer.getArea().getVerticalValue());
        ChartCollection collection = (ChartCollection) designer.getTarget().getChartCollection();
        for (int i = 0; i < iconLocations.length; i++) {
            if (iconLocations[i].contains(clikPoint)) {
                if (i == collection.getSelectedIndex()) {
                    return;
                }
                collection.setSelectedIndex(i);
                designer.repaint();
                ChartDesignEditPane.getInstance().populateSelectedTabPane();
                return;
            }
        }

        if (add.contains(clikPoint)) {
            Chart[] barChart = ColumnIndependentChart.columnChartTypes;
            try {
                Chart newChart = (Chart) barChart[0].clone();
                int select = collection.getSelectedIndex();
                collection.addNamedChartAtIndex(newChart.getTitle().getTextObject().toString(), newChart,select+1);
                collection.setSelectedIndex(select+1);
                ChartDesignEditPane.getInstance().populateSelectedTabPane();
            } catch (CloneNotSupportedException e1) {
                FRLogger.getLogger().error("Error in Clone");
            }
            designer.fireTargetModified();
            ChartDesignEditPane.getInstance().populateSelectedTabPane();
            return;
        }

        if (del != null && del.contains(clikPoint)) {
            int selectedIndex = collection.getSelectedIndex();
            collection.removeNameObject(selectedIndex);
            if (selectedIndex > 0) {
                collection.setSelectedIndex(selectedIndex - 1);
            } else {
                collection.setSelectedIndex(0);
            }
            designer.fireTargetModified();
            ChartDesignEditPane.getInstance().populateSelectedTabPane();
        }
    }

    /**
     * 鼠标悬浮上时的数据点提示
     *
     * @param e 事件
     */
    public void mouseMoved(MouseEvent e) {
        Point clikPoint = new Point(e.getPoint().x + designer.getArea().getHorizontalValue(), e.getPoint().y + designer.getArea().getVerticalValue());
        if (clikPoint.getY() < H_GAP || clikPoint.getY() > H_GAP + ICON_SIZE) {
            ToolTip4Chart.getInstance().hideToolTip();
            overIndex = -1;
            return;
        }
        ChartCollection collection = (ChartCollection) designer.getTarget().getChartCollection();
        for (int i = 0; i < iconLocations.length; i++) {
            if (iconLocations[i].contains(clikPoint)) {
                overIndex = i;
                String chartName = collection.getChartName(i);
                ToolTip4Chart.getInstance().showToolTip(chartName,e.getXOnScreen(),e.getYOnScreen());
                return;
            }
        }
        ToolTip4Chart.getInstance().hideToolTip();
        overIndex = -1;
    }

}
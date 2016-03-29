/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe;

import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
import com.fr.base.background.ColorBackground;
import com.fr.chart.base.*;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.gui.type.ColumnPlotPane4ToolBar;
import com.fr.design.mainframe.chart.gui.type.PlotPane4ToolBar;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: 下午8:32
 */
public class ChartToolBarPane extends JPanel {
    public static final int TOTAL_HEIGHT = 42;
    private static final int COM_HEIGHT = 22;
    private static final int GAP = 7;
    private static final int COM_GAP = 14;
    private static final int COMBOX_WIDTH = 230;

    private static final String[] CHOOSEITEM = new String[]{
            Inter.getLocText("FR-Chart-Type_Column"),
            Inter.getLocText("FR-Chart-Type_Line"),
            Inter.getLocText("FR-Chart-Type_Bar"),
            Inter.getLocText("FR-Chart-Type_Pie"),
            Inter.getLocText("FR-Chart-Type_Area"),
            Inter.getLocText("FR-Chart-Type_XYScatter"),
            Inter.getLocText("FR-Chart-Chart_BubbleChart"),
            Inter.getLocText("FR-Chart-Type_Radar"),
            Inter.getLocText("FR-Chart-Type_Stock"),
            Inter.getLocText("FR-Chart-Type_Meter"),
            Inter.getLocText("FR-Chart-Type_Range"),
            Inter.getLocText("FR-Chart-Type_Comb"),
            Inter.getLocText("FR-Chart-Type_Gantt"),
            Inter.getLocText("FR-Chart-Type_Donut"),
            Inter.getLocText("FR-Chart-Map_Map"),
            "gis"+Inter.getLocText("FR-Chart-Map_Map")
    };

    private UIComboBox chooseComboBox = new UIComboBox(CHOOSEITEM) {
        public Dimension getPreferredSize() {
            return new Dimension(COMBOX_WIDTH, COM_HEIGHT);
        }
    };

    private JPanel stylePane;
    private JPanel plotTypeComboBoxPane;
    private UIButton topDownShade = new UIButton(Inter.getLocText("FR-Chart-Style_TopDownShade"));
    private UIButton transparent = new UIButton(Inter.getLocText("FR-Chart-Style_Transparent"));
    private UIButton plane3D = new UIButton(Inter.getLocText("FR-Chart-Style_Plane3D"));
    private UIButton gradient = new UIButton(Inter.getLocText("FR-Chart-Style_GradientHighlight"));
    private ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                ChartToolBarPane.this.remove(centerPane);
                ChartToolBarPane.this.remove(stylePane);
                if(chooseComboBox.getSelectedIndex() < ChartTypeValueCollection.MAP.toInt()){
                    calSubChartTypesPane(chooseComboBox.getSelectedIndex());
                    ChartToolBarPane.this.add(subChartTypesPane,BorderLayout.CENTER);
                    centerPane = subChartTypesPane;
                    ChartToolBarPane.this.add(stylePane, BorderLayout.EAST);
                } else{
                    calMapSubChartTypesPane(chooseComboBox.getSelectedIndex());
                    ChartToolBarPane.this.add(mapTypePane, BorderLayout.CENTER);
                    centerPane = mapTypePane;
                }
                ChartCollection chartCollection = (ChartCollection) chartDesigner.getTarget().getChartCollection();
                Chart chart = chartCollection.getSelectedChart();
                ChartToolBarPane.this.validate();
                fireTypeChange();

                if(chooseComboBox.getSelectedIndex() == ChartTypeValueCollection.MAP.toInt()){
                    mapTypePane.populateMapPane(((MapPlot) chart.getPlot()).getMapName());
                }else if(chooseComboBox.getSelectedIndex() == ChartTypeValueCollection.GIS.toInt()){
                    mapTypePane.populateMapPane(chart.getChartName());
                }
            }
        }
    };
    private PlotPane4ToolBar subChartTypesPane;//默认柱形图

    private AbstractMapPlotPane4ToolBar mapTypePane;//地图类型选择的面板
    private JPanel centerPane;

    private ChartDesigner chartDesigner;
    private int lastStyleIndex = -1;
    private MouseAdapter styleListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            ChartCollection chartCollection = (ChartCollection) chartDesigner.getTarget().getChartCollection();
            Chart chart = chartCollection.getSelectedChart();
            Plot newPlot;
            int chartType =chart.getPlot().getPlotType().toInt();
            if(chartType >= ChartTypeValueCollection.MAP.toInt()){
               return;
            }
            newPlot = subChartTypesPane.setSelectedClonedPlotWithCondition(chart.getPlot());
            chartDesigner.fireTargetModified();
            UIButton button = (UIButton)e.getSource();
            //如果是第二次选中，就是消除
            if(button.isSelected()){
                button.setSelected(false);
                chart.setPlot(newPlot);
                resetChart(chart);
                lastStyleIndex = -1;
                ChartDesignEditPane.getInstance().populateSelectedTabPane();
                return;
            }
            clearStyleChoose();
            setStyle(chart,e,newPlot);
            lastStyleIndex = chart.getPlot().getPlotStyle();
        }
    };

    private void setStyle( Chart chart,MouseEvent e,Plot newPlot){
        if (e.getSource() == topDownShade) {
            topDownShade.setSelected(true);
            chart.setPlot(newPlot);
            chart.getPlot().setPlotStyle(ChartConstants.STYLE_SHADE);
            resetChart(chart);
            createCondition4Shade(chart);
            setPlotFillStyle(chart);
        } else if (e.getSource() == transparent) {
            transparent.setSelected(true);
            chart.setPlot(newPlot);
            chart.getPlot().setPlotStyle(ChartConstants.STYLE_TRANSPARENT);
            resetChart(chart);
            createCondition4Transparent(chart);
            setPlotFillStyle(chart);
        } else if (e.getSource() == plane3D) {
            plane3D.setSelected(true);
            chart.setPlot(newPlot);
            chart.getPlot().setPlotStyle(ChartConstants.STYLE_3D);
            resetChart(chart);
            createCondition4Plane3D(chart);
            setPlotFillStyle(chart);
        } else if (e.getSource() == gradient) {
            gradient.setSelected(true);
            chart.setPlot(newPlot);
            chart.getPlot().setPlotStyle(ChartConstants.STYLE_OUTER);
            resetChart(chart);
            createCondition4HighLight(chart);
            setPlotFillStyle(chart);
        }
        chart.setStyleGlobal(true);
        ChartEditPane pane = ChartDesignEditPane.getInstance();
        pane.styleChange(true);
        ChartDesignEditPane.getInstance().populate((ChartCollection)chartDesigner.getTarget().getChartCollection());
        pane.styleChange(false);
    }

    public ChartToolBarPane(ChartDesigner designer) {
        chartDesigner = designer;
        subChartTypesPane = new ColumnPlotPane4ToolBar(designer);//默认柱形图
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(GAP, COM_GAP, GAP, 0));
        plotTypeComboBoxPane = new JPanel();
        plotTypeComboBoxPane.setBorder(new EmptyBorder(2, 0, 2, 0));
        plotTypeComboBoxPane.setLayout(new BorderLayout());
        plotTypeComboBoxPane.add(chooseComboBox, BorderLayout.CENTER);
        chooseComboBox.addItemListener(itemListener);
        //默认选择第一个
        chooseComboBox.setSelectedIndex(0);
        this.add(plotTypeComboBoxPane, BorderLayout.WEST);
        initStylePane();
        this.add(stylePane, BorderLayout.EAST);
        this.add(subChartTypesPane, BorderLayout.CENTER);
        this.centerPane = subChartTypesPane;
        topDownShade.addMouseListener(styleListener);
        transparent.addMouseListener(styleListener);
        plane3D.addMouseListener(styleListener);
        gradient.addMouseListener(styleListener);
    }

    private void initStylePane(){
       stylePane = new JPanel() {
           public Dimension getPreferredSize() {
               Dimension size = super.getPreferredSize();
               return new Dimension(size.width, COM_HEIGHT);
           }
       };
        stylePane.setLayout(new FlowLayout(FlowLayout.LEFT, COM_GAP, 0));
        stylePane.setBorder(new EmptyBorder(3, 0, 3, 0));
        stylePane.add(topDownShade);
        stylePane.add(transparent);
        stylePane.add(plane3D);
        stylePane.add(gradient);
    }

    /**
     * 清除工具栏上面全局风格按钮的选中
     */
    public void clearStyleChoose() {
        topDownShade.setSelected(false);
        transparent.setSelected(false);
        plane3D.setSelected(false);
        gradient.setSelected(false);
    }


    private void calMapSubChartTypesPane(int index){
        ChartTypeValueCollection type = ChartTypeValueCollection.parse(index);
        mapTypePane = PlotToolBarFactory.createToolBar4MapPlot(type,chartDesigner);
    }

    private void calSubChartTypesPane(int index) {
        ChartTypeValueCollection type = ChartTypeValueCollection.parse(index);
        subChartTypesPane = PlotToolBarFactory.createToolBar4NormalPlot(type,chartDesigner);
    }


    private void fireTypeChange() {
        if(chooseComboBox.getSelectedIndex() < ChartTypeValueCollection.MAP.toInt()){
            subChartTypesPane.fireChange();
        }else{
            mapTypePane.fireChange();
        }
    }

    //图表区属性清空
    private void resetChart(Chart chart) {
        chart.setTitle(new Title(chart.getTitle().getTextObject()));
        chart.setBorderStyle(Constants.LINE_NONE);
        chart.setBorderColor(new Color(150, 150, 150));
        chart.setBackground(null);
        setPlotFillStyle(chart);
    }

    //高光渐变的默认属性设置
    private void createCondition4HighLight(Chart chart) {
        if (chart != null) {
            //标题
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.LEFT);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.BOLD, 16f, new Color(51, 51, 51)));

            //图例
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(138, 140, 139)));
            legend.setPosition(Constants.RIGHT_TOP);
            chart.getPlot().setLegend(legend);

            //分类轴,现在只有柱形图，条形图，面积图
            if (chart.getPlot() instanceof CategoryPlot) {
                CategoryPlot plot = (CategoryPlot) chart.getPlot();

                //分类轴设置
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(204, 220, 228));
                cateAxis.setTickMarkType(Constants.TICK_MARK_INSIDE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(138, 140, 139)));

                //值轴
                Axis valueAxis = plot.getyAxis();
                valueAxis.setAxisStyle(Constants.NONE);
                valueAxis.setAxisColor(null);
                valueAxis.setTickMarkType(Constants.TICK_MARK_INSIDE);
                valueAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                valueAxis.setShowAxisLabel(true);
                valueAxis.getTextAttr().setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 10f, new Color(138, 140, 139)));

                //绘图区
                plot.setBorderStyle(Constants.LINE_THIN);
                plot.setBorderColor(new Color(204, 220, 228));
                plot.setBackground(ColorBackground.getInstance(new Color(248, 247, 245)));
                plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
                plot.getyAxis().setMainGridColor(new Color(192, 192, 192));
            }

        }
    }

    //平面3D的默认属性设置
    private void createCondition4Plane3D(Chart chart) {
        if (chart != null) {
            //标题
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.CENTER);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 16f, new Color(51, 51, 51)));

            //图例
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(128, 128, 128)));
            legend.setPosition(Constants.TOP);
            chart.getPlot().setLegend(legend);

            //分类轴,现在只有柱形图，条形图，面积图
            if (chart.getPlot() instanceof CategoryPlot) {
                CategoryPlot plot = (CategoryPlot) chart.getPlot();
                //分类轴设置
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(57, 57, 57));
                cateAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(57, 57, 57)));

                //值轴设置
                Axis valueAxis = plot.getyAxis();
                valueAxis.setAxisStyle(Constants.LINE_NONE);
                valueAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                valueAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                valueAxis.setShowAxisLabel(false);

                //绘图区
                plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
                plot.getyAxis().setMainGridColor(new Color(192, 192, 192));
                chart.setBorderStyle(Constants.LINE_NONE);

                //数据标签
                ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
                DataSeriesCondition attr = attrList.getExisted(AttrContents.class);
                if (attr != null) {
                    attrList.remove(attr);
                }
                AttrContents attrContents = new AttrContents();
                attrContents.setPosition(Constants.OUTSIDE);
                attrContents.setSeriesLabel(ChartConstants.VALUE_PARA);
                attrContents.setTextAttr(new TextAttr(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(51, 51, 51))));
                attrList.addDataSeriesCondition(attrContents);
            }
        }
    }

    //透明风格的默认属性设置
    private void createCondition4Transparent(Chart chart) {
        if (chart != null) {
            //标题
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.LEFT);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.BOLD, 16f, new Color(192, 192, 192)));

            //图例
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(138, 140, 139)));
            legend.setPosition(Constants.RIGHT_TOP);
            chart.getPlot().setLegend(legend);

            Plot plot = chart.getPlot();
            //绘图区
            chart.setBackground(ColorBackground.getInstance(new Color(51, 51, 51)));

            //分类轴,现在只有柱形图，条形图，面积图
            if (plot instanceof CategoryPlot) {
                //边框
                plot.setBorderStyle(Constants.LINE_THIN);
                plot.setBorderColor(new Color(65, 65, 65));

                //分类轴设置
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(192, 192, 192));
                cateAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(150, 150, 150)));

                //值轴
                Axis valueAxis = plot.getyAxis();
                valueAxis.setShowAxisLabel(true);
                valueAxis.setAxisStyle(Constants.LINE_NONE);
                valueAxis.getTextAttr().setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 10f, new Color(150, 150, 150)));
                valueAxis.setMainGridStyle(Constants.LINE_THIN);
                valueAxis.setMainGridColor(new Color(63, 62, 62));
            }
        }
    }

    //渐变的默认属性设置
    private void createCondition4Shade(Chart chart) {
        if (chart != null) {
            //标题
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.CENTER);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.BOLD, 16f, new Color(0, 51, 102)));

            //图例
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(128, 128, 128)));
            legend.setPosition(Constants.BOTTOM);
            chart.getPlot().setLegend(legend);

            //分类轴,现在只有柱形图，条形图，面积图
            if (chart.getPlot() instanceof CategoryPlot) {
                CategoryPlot plot = (CategoryPlot) chart.getPlot();

                //分类轴设置
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(73, 100, 117));
                cateAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(128, 128, 128)));

                //值轴
                Axis valueAxis = plot.getyAxis();
                valueAxis.setShowAxisLabel(true);
                valueAxis.getTextAttr().setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 10f, new Color(128, 128, 128)));
                valueAxis.setAxisStyle(Constants.LINE_NONE);

                //绘图区
                plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
                plot.getyAxis().setMainGridColor(new Color(192, 192, 192));
                plot.setHorizontalIntervalBackgroundColor(new Color(243, 243, 243));
            }
        }
    }

    private void setPlotFillStyle(Chart chart) {
        ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
        Plot plot = chart.getPlot();
        Object preStyle = null;
        String name = "";
        if (topDownShade.isSelected()) {
            name = Inter.getLocText("FR-Chart-Style_Retro");
            preStyle = manager.getPreStyle(name);
        } else if (transparent.isSelected()) {
            name = Inter.getLocText("FR-Chart-Style_Fresh");
            preStyle = manager.getPreStyle(name);
        } else if (plane3D.isSelected()) {
            name = Inter.getLocText("FR-Chart-Style_Bright");
            preStyle = manager.getPreStyle(name);
        } else if (gradient.isSelected()) {
            name = Inter.getLocText("FR-Chart-Style_Bright");
            preStyle = manager.getPreStyle(name);
        }else{
            preStyle = null;
        }
        if (preStyle == null) {
            plot.getPlotFillStyle().setColorStyle(ChartConstants.COLOR_DEFAULT);
        } else {
            AttrFillStyle fillStyle = ((ChartPreStyle) preStyle).getAttrFillStyle();
            fillStyle.setFillStyleName(name);
            plot.setPlotFillStyle(fillStyle);
        }

    }

    public void populate(){
        ChartCollection chartCollection = (ChartCollection) chartDesigner.getTarget().getChartCollection();
        Chart chart = chartCollection.getSelectedChart();
        chooseComboBox.removeItemListener(itemListener);
        chooseComboBox.setSelectedIndex(chart.getPlot().getPlotType().toInt());
        int chartType =chart.getPlot().getPlotType().toInt();
        this.removeAll();
        populateStyle();
        this.add(plotTypeComboBoxPane, BorderLayout.WEST);
        initStylePane();
        if(chartType < ChartTypeValueCollection.MAP.toInt()){
            calSubChartTypesPane(chartType);
            subChartTypesPane.setSelectedIndex(chart.getPlot().getDetailType());
            ChartToolBarPane.this.add(subChartTypesPane, BorderLayout.CENTER);
            this.add(subChartTypesPane, BorderLayout.CENTER);
            centerPane = subChartTypesPane;
            this.add(stylePane, BorderLayout.EAST);
        }else if(chartType == ChartTypeValueCollection.MAP.toInt()){
            calMapSubChartTypesPane(chartType);
            mapTypePane.populateMapPane(((MapPlot) chart.getPlot()).getMapName());
            ChartToolBarPane.this.add(mapTypePane, BorderLayout.CENTER);
            centerPane = mapTypePane;
        }else{
            calMapSubChartTypesPane(chartType);
            mapTypePane.populateMapPane((chart.getPlot()).getPlotName());
            ChartToolBarPane.this.add(mapTypePane, BorderLayout.CENTER);
            centerPane = mapTypePane;
        }
        ChartToolBarPane.this.validate();
        chooseComboBox.addItemListener(itemListener);
    }



    private void populateStyle() {
        clearStyleChoose();
        ChartCollection chartCollection = (ChartCollection) chartDesigner.getTarget().getChartCollection();
        Chart chart = chartCollection.getSelectedChart();
        int plotStyle = chart.getPlot().getPlotStyle();
        switch (plotStyle) {
            case ChartConstants.STYLE_SHADE:
                topDownShade.setSelected(chart.isStyleGlobal() && true);
                break;
            case ChartConstants.STYLE_TRANSPARENT:
                transparent.setSelected(chart.isStyleGlobal() && true);
                break;
            case ChartConstants.STYLE_3D:
                plane3D.setSelected(chart.isStyleGlobal() && true);
                break;
            case ChartConstants.STYLE_OUTER:
                gradient.setSelected(chart.isStyleGlobal() && true);
                break;
            default:
                clearStyleChoose();
                break;
        }
        lastStyleIndex = plotStyle;
    }


}
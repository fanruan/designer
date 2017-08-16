package com.fr.plugin.chart.designer.style.background;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.attr.DefaultAxisHelper;
import com.fr.plugin.chart.attr.axis.VanChartAlertValue;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.VanChartCustomIntervalBackground;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartUIMenuNameableCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 样式-背景-绘图区背景-坐标轴图表特有（间隔背景、网格线、警戒线）
 */
public class VanChartAxisAreaPane extends BasicBeanPane<Plot>{
    private static final long serialVersionUID = -1880497996650835504L;

    protected ColorSelectBox horizontalGridLine;
    protected ColorSelectBox verticalGridLine;

    protected UICorrelationComboBoxPane alertLine;

    private UIButtonGroup isDefaultIntervalBackground;
    private JPanel centerPane;
    private CardLayout cardLayout;
    protected ColorSelectBox horizontalColorBackground;
    private ColorSelectBox verticalColorBackground;
    protected UICorrelationComboBoxPane customIntervalBackground;

    public VanChartAxisAreaPane(){
        initComponents();
    }

    protected void initComponents(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { f };
        double[] rowSize = {p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{createGridLinePane(new double[]{p,p,p}, new double[]{p,f})},
                new Component[]{createAlertLinePane()},
                new Component[]{createIntervalPane(new double[]{p,p,p,p}, new double[]{p,f})},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    private JPanel createGridLinePane(double[] row, double[] col){
        horizontalGridLine = new ColorSelectBox(100);
        verticalGridLine = new ColorSelectBox(100);
        Component[][] components = getGridLinePaneComponents();
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("ChartF-Grid_Line"),panel);
    }

    protected Component[][] getGridLinePaneComponents() {
        return new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Direction_Horizontal")),horizontalGridLine},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Direction_Vertical")),verticalGridLine},
        };
    }

    protected JPanel createAlertLinePane(){
        alertLine = new UICorrelationComboBoxPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_AlertLine"),alertLine);
    }

    protected JPanel createIntervalPane(double[] row, double[] col){
        isDefaultIntervalBackground = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_IntervalBackground"), Inter.getLocText("Plugin-ChartF_CustomIntervalBackground")});
        horizontalColorBackground = new ColorSelectBox(100);
        verticalColorBackground = new ColorSelectBox(100);
        Component[][] components = getIntervalPaneComponents();
        JPanel defaultPane = TableLayoutHelper.createTableLayoutPane(components, row, col);
        customIntervalBackground = new UICorrelationComboBoxPane();

        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout);
        centerPane.add(defaultPane, Inter.getLocText("Plugin-ChartF_IntervalBackground"));
        centerPane.add(customIntervalBackground, Inter.getLocText("Plugin-ChartF_CustomIntervalBackground"));
        isDefaultIntervalBackground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCardPane();
            }
        });
        JPanel intervalPane = new JPanel(new BorderLayout(0, 6));
        intervalPane.add(isDefaultIntervalBackground, BorderLayout.NORTH);
        intervalPane.add(centerPane, BorderLayout.CENTER);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_IntervalBackground"),intervalPane);
    }

    protected Component[][] getIntervalPaneComponents() {
        return new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Direction_Horizontal")),horizontalColorBackground},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Direction_Vertical")),verticalColorBackground},
        };
    }

    private void checkCardPane(){
        if(isDefaultIntervalBackground.getSelectedIndex() == 0){
            cardLayout.show(centerPane, Inter.getLocText("Plugin-ChartF_IntervalBackground"));
        } else {
            cardLayout.show(centerPane, Inter.getLocText("Plugin-ChartF_CustomIntervalBackground"));
        }
    }

    protected String title4PopupWindow(){
        return "";
    }

    public void populateBean(Plot plot){
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot)plot;
        List<VanChartAxis> xAxisList = rectanglePlot.getXAxisList();
        List<VanChartAxis> yAxisList = rectanglePlot.getYAxisList();
        String[] axisNames = DefaultAxisHelper.getAllAxisNames(xAxisList, yAxisList);

        populateGridLine(rectanglePlot);

        populateAlert(xAxisList, yAxisList, axisNames);

        isDefaultIntervalBackground.setSelectedIndex(rectanglePlot.isDefaultIntervalBackground() ? 0 : 1);
        horizontalColorBackground.setSelectObject(rectanglePlot.getDefaultYAxis().getDefaultIntervalBackgroundColor());
        verticalColorBackground.setSelectObject(rectanglePlot.getDefaultXAxis().getDefaultIntervalBackgroundColor());
        populateCustomIntervalBackground(xAxisList, yAxisList, axisNames);
        checkCardPane();
    }

    protected void populateGridLine(VanChartRectanglePlot rectanglePlot) {
        horizontalGridLine.setSelectObject(rectanglePlot.getDefaultYAxis().getMainGridColor());
        verticalGridLine.setSelectObject(rectanglePlot.getDefaultXAxis().getMainGridColor());
    }

    protected Class<? extends BasicBeanPane> getAlertPaneClass() {
        return VanChartAlertValuePane.class;
    }

    protected void setAlertDemoAxisName(VanChartAlertValue demo, String[] axisNames) {
        demo.setAxisName(axisNames[0]);
    }

    private void populateAlert(List<VanChartAxis> xAxisList, List<VanChartAxis> yAxisList, String[] axisNames){
        List<UIMenuNameableCreator> menuList = new ArrayList<UIMenuNameableCreator>();
        VanChartAlertValue demo = new VanChartAlertValue();
        demo.setAxisNamesArray(axisNames);
        setAlertDemoAxisName(demo, axisNames);
        menuList.add(new VanChartUIMenuNameableCreator(Inter.getLocText("Plugin-ChartF_AlertLine"), demo, getAlertPaneClass()));

        alertLine.refreshMenuAndAddMenuAction(menuList);

        List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        for(VanChartAxis axis: xAxisList){
            List<VanChartAlertValue> values = axis.getAlertValues();
            for(VanChartAlertValue alertValue : values) {
                alertValue.setAxisNamesArray(axisNames);
                alertValue.setAxisName(axis.getAxisName());
                list.add(new VanChartUIMenuNameableCreator(alertValue.getAlertPaneSelectName(), alertValue, getAlertPaneClass()));
            }
        }

        for(VanChartAxis axis: yAxisList){
            List<VanChartAlertValue> values = axis.getAlertValues();
            for(VanChartAlertValue alertValue : values) {
                alertValue.setAxisNamesArray(axisNames);
                alertValue.setAxisName(axis.getAxisName());
                list.add(new VanChartUIMenuNameableCreator(alertValue.getAlertPaneSelectName(), alertValue, getAlertPaneClass()));
            }
        }

        alertLine.populateBean(list);
        alertLine.doLayout();
    }

    protected Class<? extends BasicBeanPane> getIntervalPaneClass() {
        return VanChartCustomIntervalBackgroundPane.class;
    }

    protected void setCustomIntervalBackgroundDemoAxisName(VanChartCustomIntervalBackground demo, String[] axisNames) {
        demo.setAxisName(axisNames[0]);
    }

    private void populateCustomIntervalBackground(List<VanChartAxis> xAxisList, List<VanChartAxis> yAxisList, String[] axisNames){
        List<UIMenuNameableCreator> menuList = new ArrayList<UIMenuNameableCreator>();
        VanChartCustomIntervalBackground demo = new VanChartCustomIntervalBackground();
        demo.setAxisNamesArray(axisNames);
        setCustomIntervalBackgroundDemoAxisName(demo, axisNames);
        menuList.add(new VanChartUIMenuNameableCreator(Inter.getLocText("Plugin-ChartF_CustomIntervalBackground"), demo, getIntervalPaneClass()));

        customIntervalBackground.refreshMenuAndAddMenuAction(menuList);

        List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        for(VanChartAxis axis: xAxisList){
            List<VanChartCustomIntervalBackground> customIntervalBackgrounds = axis.getCustomIntervalBackgroundArray();
            for(VanChartCustomIntervalBackground background : customIntervalBackgrounds){
                background.setAxisNamesArray(axisNames);
                background.setAxisName(axis.getAxisName());
                list.add(new VanChartUIMenuNameableCreator(background.getCustomIntervalBackgroundSelectName(), background, getIntervalPaneClass()));
            }

        }

        for(VanChartAxis axis: yAxisList){
            List<VanChartCustomIntervalBackground> customIntervalBackgrounds = axis.getCustomIntervalBackgroundArray();
            for(VanChartCustomIntervalBackground background : customIntervalBackgrounds){
                background.setAxisNamesArray(axisNames);
                background.setAxisName(axis.getAxisName());
                list.add(new VanChartUIMenuNameableCreator(background.getCustomIntervalBackgroundSelectName(), background, getIntervalPaneClass()));
            }
        }

        customIntervalBackground.populateBean(list);
        customIntervalBackground.doLayout();
    }

    public void updateBean(Plot plot){
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot)plot;
        List<VanChartAxis> xAxisList = rectanglePlot.getXAxisList();
        List<VanChartAxis> yAxisList = rectanglePlot.getYAxisList();

        updateGirdLine(rectanglePlot);

        updateAlert(xAxisList, yAxisList);

        rectanglePlot.setIsDefaultIntervalBackground(isDefaultIntervalBackground.getSelectedIndex() == 0);
        if(rectanglePlot.isDefaultIntervalBackground()){
            rectanglePlot.getDefaultYAxis().setDefaultIntervalBackgroundColor(horizontalColorBackground.getSelectObject());
            rectanglePlot.getDefaultXAxis().setDefaultIntervalBackgroundColor(verticalColorBackground.getSelectObject());
        } else {
            rectanglePlot.getDefaultYAxis().setDefaultIntervalBackgroundColor(null);
            rectanglePlot.getDefaultXAxis().setDefaultIntervalBackgroundColor(null);
        }
        updateCustomIntervalBackground(xAxisList, yAxisList);
    }

    protected void updateGirdLine(VanChartRectanglePlot rectanglePlot) {
        rectanglePlot.getDefaultYAxis().setMainGridColor(horizontalGridLine.getSelectObject());
        rectanglePlot.getDefaultXAxis().setMainGridColor(verticalGridLine.getSelectObject());
    }

    private void updateAlert(List<VanChartAxis> xAxisList, List<VanChartAxis> yAxisList){

        List<UIMenuNameableCreator> alertList = alertLine.updateBean();

        for(VanChartAxis axis : xAxisList){
            List<VanChartAlertValue> axisAlerts = new ArrayList<VanChartAlertValue>();
            for(UIMenuNameableCreator creator : alertList) {
                VanChartAlertValue value = (VanChartAlertValue)creator.getObj();
                if(ComparatorUtils.equals(value.getAxisName(), axis.getAxisName())){
                    value.setAlertPaneSelectName(creator.getName());
                    axisAlerts.add(value);
                }
            }
            axis.setAlertValues(axisAlerts);
        }
        for(VanChartAxis axis : yAxisList){
            List<VanChartAlertValue> axisAlerts = new ArrayList<VanChartAlertValue>();
            for(UIMenuNameableCreator creator : alertList) {
                VanChartAlertValue value = (VanChartAlertValue)creator.getObj();
                if(ComparatorUtils.equals(value.getAxisName(), axis.getAxisName()) || yAxisEquals(value.getAxisName(), axis.getAxisName())){
                    value.setAlertPaneSelectName(creator.getName());
                    axisAlerts.add(value);
                }
            }
            axis.setAlertValues(axisAlerts);
        }
    }

    /**
     * Y軸和雷達圖的極軸也是相等的
     * @param axisName
     * @param valueAxisName
     * @return
     */
    private boolean yAxisEquals(String valueAxisName, String axisName) {
        return ComparatorUtils.equals(VanChartAttrHelper.Y_AXIS_PREFIX, valueAxisName) &&
                ComparatorUtils.equals(VanChartAttrHelper.RADAR_Y_AXIS_PREFIX, axisName);
    }

    private void updateCustomIntervalBackground(List<VanChartAxis> xAxisList, List<VanChartAxis> yAxisList){

        List<UIMenuNameableCreator> customList = customIntervalBackground.updateBean();

        for(VanChartAxis axis : xAxisList){
            List<VanChartCustomIntervalBackground> axisCustomBackground = new ArrayList<VanChartCustomIntervalBackground>();
            if(isDefaultIntervalBackground.getSelectedIndex() == 1){//tab选中间隔背景，则置所有自定义间隔背景为空数组
                for(UIMenuNameableCreator creator : customList) {
                    VanChartCustomIntervalBackground value = (VanChartCustomIntervalBackground)creator.getObj();
                    if(ComparatorUtils.equals(value.getAxisName(), axis.getAxisName())){
                        value.setCustomIntervalBackgroundSelectName(creator.getName());
                        axisCustomBackground.add(value);
                    }
                }
            }
            axis.setCustomIntervalBackgroundArray(axisCustomBackground);
        }
        for(VanChartAxis axis : yAxisList){
            List<VanChartCustomIntervalBackground> axisCustomBackground = new ArrayList<VanChartCustomIntervalBackground>();
            if(isDefaultIntervalBackground.getSelectedIndex() == 1){//tab选中间隔背景，则置所有自定义间隔背景为空数组
                for(UIMenuNameableCreator creator : customList) {
                    VanChartCustomIntervalBackground value = (VanChartCustomIntervalBackground)creator.getObj();
                    if(ComparatorUtils.equals(value.getAxisName(), axis.getAxisName())){
                        value.setCustomIntervalBackgroundSelectName(creator.getName());
                        axisCustomBackground.add(value);
                    }
                }
            }
            axis.setCustomIntervalBackgroundArray(axisCustomBackground);
        }
    }


    public Plot updateBean(){
        return null;
    }
}
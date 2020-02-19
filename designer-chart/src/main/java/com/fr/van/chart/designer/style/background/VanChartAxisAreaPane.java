package com.fr.van.chart.designer.style.background;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * 样式-背景-绘图区背景-坐标轴图表特有（间隔背景、网格线、警戒线）
 */
public class VanChartAxisAreaPane extends BasicBeanPane<Plot> {
    private static final long serialVersionUID = -1880497996650835504L;

    protected ColorSelectBox horizontalGridLine;
    protected ColorSelectBox verticalGridLine;

    protected AlertLineListControlPane alertLine;

    private UIButtonGroup isDefaultIntervalBackground;
    private JPanel centerPane;
    private CardLayout cardLayout;
    protected ColorSelectBox horizontalColorBackground;
    private ColorSelectBox verticalColorBackground;
    protected BackgroundListControlPane customIntervalBackground;

    //线型支持虚线 恢复用注释。取消注释。
//    private LineTypeComboBox horizonLineType;//横向线型
//    private LineTypeComboBox verticalLineType;//纵向线型
//    private JPanel horizontalColorPane;
//    private JPanel verticalColorPane;

    public VanChartAxisAreaPane() {
        initComponents();
    }

    protected void initComponents() {
        horizontalGridLine = new ColorSelectBox(100);
        verticalGridLine = new ColorSelectBox(100);
        //线型支持虚线 恢复用注释。取消注释。
//        horizonLineType = new LineTypeComboBox(new LineType[]{LineType.NONE, LineType.NORMAL, LineType.DASH});
//        verticalLineType = new LineTypeComboBox(new LineType[]{LineType.NONE, LineType.NORMAL, LineType.DASH});

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double s = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
        double[] columnSize = {f};
        double[] rowSize = {p, p, p};

        Component[][] components = new Component[][]{
                new Component[]{createGridLinePane()},
                new Component[]{createAlertLinePane()},
                new Component[]{createIntervalPane(new double[]{p, p, p, p}, new double[]{f, s})},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    //线型支持虚线 恢复用注释。删除下面方法。
    protected JPanel createGridLinePane() {
        horizontalGridLine = new ColorSelectBox(100);
        verticalGridLine = new ColorSelectBox(100);
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Direction_Horizontal")), horizontalGridLine},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Direction_Vertical")), verticalGridLine},
        };
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] row = new double[components.length];
        Arrays.fill(row, p);
        double[] col = {f, e};
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Grid_Line"), panel);
    }

    //线型支持虚线 恢复用注释。取消注释。
//    protected JPanel createGridLinePane() {
//
//        Component[][] upComponent = new Component[][]{
//                new Component[]{null, null},
//                new Component[]{null, horizontalGridLine}
//        };
//        horizontalColorPane = TableLayout4VanChartHelper.createGapTableLayoutPane(upComponent);
//
//        Component[][] downComponent = new Component[][]{
//                new Component[]{null,null},
//                new Component[]{null, verticalGridLine}
//        };
//        verticalColorPane = TableLayout4VanChartHelper.createGapTableLayoutPane(downComponent);
//
//        horizonLineType.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (horizontalGridLine == null || horizonLineType == null){
//                    return;
//                }
//                horizontalColorPane.setVisible(horizonLineType.getSelectedItem() != LineType.NONE);
//            }
//        });
//
//        verticalLineType.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (verticalGridLine == null || verticalLineType == null){
//                    return;
//                }
//                verticalColorPane.setVisible(verticalLineType.getSelectedItem() != LineType.NONE);
//            }
//        });
//
//        checkColorBoxVisible();
//
//        JPanel horizonLineTypePane = TableLayout4VanChartHelper.createGapTableLayoutPane(Toolkit.i18nText("Fine-Design_Chart_Direction_Horizontal"), horizonLineType);
//        JPanel horizontal = new JPanel(new BorderLayout());
//        horizontal.add(horizonLineTypePane, BorderLayout.NORTH);
//        horizontal.add(horizontalColorPane, BorderLayout.CENTER);
//
//        JPanel verticalLineTypePane = TableLayout4VanChartHelper.createGapTableLayoutPane(Toolkit.i18nText("Fine-Design_Chart_Direction_Vertical"), verticalLineType);
//        JPanel vertical = new JPanel(new BorderLayout());
//        vertical.add(verticalLineTypePane, BorderLayout.NORTH);
//        vertical.add(verticalColorPane, BorderLayout.CENTER);
//
//        JPanel panel = new JPanel(new BorderLayout(0, 4));
//        panel.add(horizontal, BorderLayout.NORTH);
//        panel.add(vertical, BorderLayout.CENTER);
//
//        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Grid_Line"), panel);
//    }

    protected JPanel createAlertLinePane() {
        alertLine = getAlertLinePane();
        JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Alert_Line"), alertLine);
        alertLine.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        return panel;
    }

    protected AlertLineListControlPane getAlertLinePane () {
        return new AlertLineListControlPane();
    }

    protected JPanel createIntervalPane(double[] row, double[] col) {
        isDefaultIntervalBackground = new UIButtonGroup(new String[]{Toolkit.i18nText("Fine-Design_Chart_Default_Interval"), Toolkit.i18nText("Fine-Design_Chart_Custom_Interval_Background")});
        horizontalColorBackground = new ColorSelectBox(100);
        verticalColorBackground = new ColorSelectBox(100);
        Component[][] components = getIntervalPaneComponents();
        JPanel defaultPane = TableLayoutHelper.createTableLayoutPane(components, row, col);
        defaultPane.setBorder(BorderFactory.createEmptyBorder(0,12,0,0));
        customIntervalBackground = getBackgroundListControlPane();

        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout);
        centerPane.add(defaultPane, Toolkit.i18nText("Fine-Design_Chart_Default_Interval"));
        centerPane.add(customIntervalBackground, Toolkit.i18nText("Fine-Design_Chart_Custom_Interval_Background"));
        isDefaultIntervalBackground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCardPane();
            }
        });
        JPanel intervalPane = new JPanel(new BorderLayout(0, 6));
        JPanel panel1 = TableLayout4VanChartHelper.createGapTableLayoutPane(Toolkit.i18nText("Fine-Design_Chart_Interval_Background"), isDefaultIntervalBackground);
        intervalPane.add(panel1, BorderLayout.NORTH);
        intervalPane.add(centerPane, BorderLayout.CENTER);
        JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Interval_Background"), intervalPane);
        intervalPane.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        return panel;
    }

    protected BackgroundListControlPane getBackgroundListControlPane() {
        return new BackgroundListControlPane();
    }

    protected Component[][] getIntervalPaneComponents() {
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Direction_Horizontal")), horizontalColorBackground},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Direction_Vertical")), verticalColorBackground},
        };
    }

    private void checkCardPane() {
        if (isDefaultIntervalBackground.getSelectedIndex() == 0) {
            cardLayout.show(centerPane, Toolkit.i18nText("Fine-Design_Chart_Default_Interval"));
        } else {
            cardLayout.show(centerPane, Toolkit.i18nText("Fine-Design_Chart_Custom_Interval_Background"));
        }
    }

    protected String title4PopupWindow() {
        return "";
    }

    public void populateBean(Plot plot) {
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;
        checkColorBoxVisible();


        populateGridLine(rectanglePlot);

        alertLine.populate(plot);

        isDefaultIntervalBackground.setSelectedIndex(rectanglePlot.isDefaultIntervalBackground() ? 0 : 1);
        horizontalColorBackground.setSelectObject(rectanglePlot.getDefaultYAxis().getDefaultIntervalBackgroundColor());
        verticalColorBackground.setSelectObject(rectanglePlot.getDefaultXAxis().getDefaultIntervalBackgroundColor());
        customIntervalBackground.populate(plot);
        checkCardPane();
    }

    private void populateGridLine(VanChartRectanglePlot rectanglePlot) {
        horizontalGridLine.setSelectObject(rectanglePlot.getDefaultYAxis().getMainGridColor());
        verticalGridLine.setSelectObject(rectanglePlot.getDefaultXAxis().getMainGridColor());
//线型支持虚线 恢复用注释。取消注释。
//        horizonLineType.setSelectedItem(rectanglePlot.getDefaultYAxis().getGridLineType());
//        verticalLineType.setSelectedItem(rectanglePlot.getDefaultXAxis().getGridLineType());
    }


    public void updateBean(Plot plot) {
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;

        updateGirdLine(rectanglePlot);

        alertLine.update(plot);

        rectanglePlot.setIsDefaultIntervalBackground(isDefaultIntervalBackground.getSelectedIndex() == 0);
        if (rectanglePlot.isDefaultIntervalBackground()) {
            rectanglePlot.getDefaultYAxis().setDefaultIntervalBackgroundColor(horizontalColorBackground.getSelectObject());
            rectanglePlot.getDefaultXAxis().setDefaultIntervalBackgroundColor(verticalColorBackground.getSelectObject());
        } else {
            rectanglePlot.getDefaultYAxis().setDefaultIntervalBackgroundColor(null);
            rectanglePlot.getDefaultXAxis().setDefaultIntervalBackgroundColor(null);
        }
        customIntervalBackground.update(plot, isDefaultIntervalBackground.getSelectedIndex() == 0);
    }

    private void updateGirdLine(VanChartRectanglePlot rectanglePlot) {
        rectanglePlot.getDefaultYAxis().setMainGridColor(horizontalGridLine.getSelectObject());
        rectanglePlot.getDefaultXAxis().setMainGridColor(verticalGridLine.getSelectObject());
        //线型支持虚线 恢复用注释。取消注释。
//        rectanglePlot.getDefaultYAxis().setGridLineType((LineType)horizonLineType.getSelectedItem());
//        rectanglePlot.getDefaultXAxis().setGridLineType((LineType)verticalLineType.getSelectedItem());
    }

    /**
     * Y軸和雷達圖的極軸也是相等的
     *
     * @param axisName
     * @param valueAxisName
     * @return
     */
    private boolean yAxisEquals(String valueAxisName, String axisName) {
        return ComparatorUtils.equals(VanChartAttrHelper.Y_AXIS_PREFIX, valueAxisName) &&
                ComparatorUtils.equals(VanChartAttrHelper.RADAR_Y_AXIS_PREFIX, axisName);
    }

    public Plot updateBean() {
        return null;
    }

    private void checkColorBoxVisible() {
//线型支持虚线 恢复用注释。取消注释。
//        if (horizontalColorPane != null && horizonLineType != null){
//            horizontalColorPane.setVisible(horizonLineType.getSelectedItem() != LineType.NONE);
//        }
//
//        if (verticalColorPane != null && verticalLineType != null){
//            verticalColorPane.setVisible(verticalLineType.getSelectedItem() != LineType.NONE);
//        }
    }
}
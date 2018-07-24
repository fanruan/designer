package com.fr.van.chart.designer.style.background;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
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

    public VanChartAxisAreaPane() {
        initComponents();
    }

    protected void initComponents() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double s = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
        double[] columnSize = {f};
        double[] rowSize = {p, p, p};

        Component[][] components = new Component[][]{
                new Component[]{createGridLinePane(new double[]{p, p, p}, new double[]{f, e})},
                new Component[]{createAlertLinePane()},
                new Component[]{createIntervalPane(new double[]{p, p, p, p}, new double[]{f, s})},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    private JPanel createGridLinePane(double[] row, double[] col) {
        horizontalGridLine = new ColorSelectBox(100);
        verticalGridLine = new ColorSelectBox(100);
        Component[][] components = getGridLinePaneComponents();
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("ChartF-Grid_Line"), panel);
    }

    protected Component[][] getGridLinePaneComponents() {
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Direction_Horizontal")), horizontalGridLine},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Direction_Vertical")), verticalGridLine},
        };
    }

    protected JPanel createAlertLinePane() {
        alertLine = getAlertLinePane();
        JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_AlertLine"), alertLine);
        alertLine.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        return panel;
    }

    protected AlertLineListControlPane getAlertLinePane () {
        return new AlertLineListControlPane();
    }

    protected JPanel createIntervalPane(double[] row, double[] col) {
        isDefaultIntervalBackground = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Default_Interval"), com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_CustomIntervalBackground")});
        horizontalColorBackground = new ColorSelectBox(100);
        verticalColorBackground = new ColorSelectBox(100);
        Component[][] components = getIntervalPaneComponents();
        JPanel defaultPane = TableLayoutHelper.createTableLayoutPane(components, row, col);
        defaultPane.setBorder(BorderFactory.createEmptyBorder(0,12,0,0));
        customIntervalBackground = getBackgroundListControlPane();

        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout);
        centerPane.add(defaultPane, com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Default_Interval"));
        centerPane.add(customIntervalBackground, com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_CustomIntervalBackground"));
        isDefaultIntervalBackground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCardPane();
            }
        });
        JPanel intervalPane = new JPanel(new BorderLayout(0, 6));
        JPanel panel1 = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Chart_Interval_Back"), isDefaultIntervalBackground);
        intervalPane.add(panel1, BorderLayout.NORTH);
        intervalPane.add(centerPane, BorderLayout.CENTER);
        JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_IntervalBackground"), intervalPane);
        intervalPane.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        return panel;
    }

    protected BackgroundListControlPane getBackgroundListControlPane() {
        return new BackgroundListControlPane();
    }

    protected Component[][] getIntervalPaneComponents() {
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Direction_Horizontal")), horizontalColorBackground},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Direction_Vertical")), verticalColorBackground},
        };
    }

    private void checkCardPane() {
        if (isDefaultIntervalBackground.getSelectedIndex() == 0) {
            cardLayout.show(centerPane, com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Default_Interval"));
        } else {
            cardLayout.show(centerPane, com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_CustomIntervalBackground"));
        }
    }

    protected String title4PopupWindow() {
        return "";
    }

    public void populateBean(Plot plot) {
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;


        populateGridLine(rectanglePlot);

        alertLine.populate(plot);

        isDefaultIntervalBackground.setSelectedIndex(rectanglePlot.isDefaultIntervalBackground() ? 0 : 1);
        horizontalColorBackground.setSelectObject(rectanglePlot.getDefaultYAxis().getDefaultIntervalBackgroundColor());
        verticalColorBackground.setSelectObject(rectanglePlot.getDefaultXAxis().getDefaultIntervalBackgroundColor());
        customIntervalBackground.populate(plot);
        checkCardPane();
    }

    protected void populateGridLine(VanChartRectanglePlot rectanglePlot) {
        horizontalGridLine.setSelectObject(rectanglePlot.getDefaultYAxis().getMainGridColor());
        verticalGridLine.setSelectObject(rectanglePlot.getDefaultXAxis().getMainGridColor());
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

    protected void updateGirdLine(VanChartRectanglePlot rectanglePlot) {
        rectanglePlot.getDefaultYAxis().setMainGridColor(horizontalGridLine.getSelectObject());
        rectanglePlot.getDefaultXAxis().setMainGridColor(verticalGridLine.getSelectObject());
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
}
package com.fr.plugin.chart.gantt.designer.style.axis;

import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.plugin.chart.gantt.attr.GanttTimeAxis;
import com.fr.plugin.chart.type.ZoomLevel;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hufan on 2017/1/12.
 */
public class GanttTimeAxisPane extends BasicScrollPane<VanChart> {
    private static final ZoomLevel[] ZOOM_LEVELS = new ZoomLevel[]{ZoomLevel.AUTO, ZoomLevel.ZERO, ZoomLevel.ONE,
            ZoomLevel.TWO, ZoomLevel.THREE, ZoomLevel.FOUR, ZoomLevel.FIVE, ZoomLevel.SIX, ZoomLevel.SEVEN,
            ZoomLevel.EIGHT, ZoomLevel.NINE, ZoomLevel.TEN, ZoomLevel.ELEVEN, ZoomLevel.TWELVE};

    private UIButtonGroup timeZoom;
    private UIComboBox initialLevel;
    private UIButtonGroup weekendTooltip;

    private GanttAxisStylePane upHeadPane;
    private GanttAxisStylePane downHeadPane;

    @Override
    protected JPanel createContentPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p,p,p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{createConditionConfigPane()},
                new Component[]{new JSeparator()},
                new Component[]{createUpHeadPane()},
                new Component[]{new JSeparator()},
                new Component[]{createDownHeadPane()},
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createMatteBorder(10, 10, 20, 10, original));
        this.add(leftcontentPane);
    }

    private Component createDownHeadPane() {
        downHeadPane = new GanttAxisStylePane();

        return TableLayoutHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Down_Head_Table"),downHeadPane);
    }

    private Component createUpHeadPane() {
        upHeadPane = new GanttAxisStylePane();

        return TableLayoutHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Up_Head_Table"),upHeadPane);
    }

    private Component createConditionConfigPane() {
        timeZoom = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});
        initialLevel = new UIComboBox(ZOOM_LEVELS);
        weekendTooltip = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p};
        double[] col = {p, f};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Time_Zoom"), SwingConstants.RIGHT), timeZoom},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Initial_Level"), SwingConstants.RIGHT), initialLevel},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Weekend_Tooltip"), SwingConstants.RIGHT), weekendTooltip}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);

        return TableLayoutHelper.createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Condition_Config"),panel);
    }

    @Override
    public void populateBean(VanChart chart) {
        VanChartGanttPlot ganttPlot = (VanChartGanttPlot) chart.getPlot();
        GanttTimeAxis timeAxis = ganttPlot.getTimeAxis();

        timeZoom.setSelectedIndex(timeAxis.isTimeZoom() ? 0 : 1);
        initialLevel.setSelectedItem(timeAxis.getInitialZoomLevel());
        weekendTooltip.setSelectedIndex(timeAxis.isWeekendTooltip() ? 0 : 1);

        upHeadPane.populateBean(timeAxis.getUpHeaderStyle());
        downHeadPane.populateBean(timeAxis.getDownHeaderStyle());
    }

    public void updateBean(VanChart chart){
        VanChartGanttPlot ganttPlot = (VanChartGanttPlot) chart.getPlot();
        GanttTimeAxis timeAxis = new GanttTimeAxis();

        timeAxis.setTimeZoom(timeZoom.getSelectedIndex() == 0);
        timeAxis.setInitialZoomLevel((ZoomLevel) initialLevel.getSelectedItem());
        timeAxis.setWeekendTooltip(weekendTooltip.getSelectedIndex() == 0);

        upHeadPane.updateBean(timeAxis.getUpHeaderStyle());
        downHeadPane.updateBean(timeAxis.getDownHeaderStyle());

        ganttPlot.setTimeAxis(timeAxis);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_Time_Axis");
    }
}

package com.fr.van.chart.gantt.designer.style.axis;

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
import com.fr.van.chart.designer.AbstractVanChartScrollPane;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by hufan on 2017/1/12.
 */
public class GanttTimeAxisPane extends AbstractVanChartScrollPane<VanChart> {
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
                new Component[]{createUpHeadPane()},
                new Component[]{createDownHeadPane()},
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        this.add(leftcontentPane);
    }

    private Component createDownHeadPane() {
        downHeadPane = new GanttAxisStylePane();

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Down_Head_Table"),downHeadPane);
    }

    private Component createUpHeadPane() {
        upHeadPane = new GanttAxisStylePane();

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Up_Head_Table"),upHeadPane);
    }

    private Component createConditionConfigPane() {
        timeZoom = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});
        initialLevel = new UIComboBox(ZOOM_LEVELS);
        weekendTooltip = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] row = {p, p, p, p};
        double[] col = {f, e};

        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Time_Zoom")), timeZoom},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Initial_Level")), initialLevel},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Weekend_Tooltip")), weekendTooltip}
        };

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Condition_Config"),panel);
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

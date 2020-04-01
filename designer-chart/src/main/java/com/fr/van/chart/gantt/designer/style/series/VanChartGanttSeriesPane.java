package com.fr.van.chart.gantt.designer.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.ColorSelectBoxWithOutTransparent;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.stable.CoreConstants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.VanChartBeautyPane;
import com.fr.van.chart.designer.component.VanChartMarkerPane;
import com.fr.van.chart.designer.component.marker.VanChartCommonMarkerPane;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by hufan on 2017/1/13.
 */
public class VanChartGanttSeriesPane extends VanChartAbstractPlotSeriesPane {
    private UIButtonGroup seriesNewLine;

    private LineComboBox lineWidth;//线型
    private ColorSelectBoxWithOutTransparent colorSelect;//颜色

    public VanChartGanttSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    @Override
    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p,p,p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{createGanntStylePane()},
                new Component[]{createLinkLinePane()},
                new Component[]{createMarkerPane()}
        };

        contentPane = TableLayoutHelper.createTableLayoutPane(components, row, col);
        return contentPane;
    }

    private JPanel createGanntStylePane(){
        seriesNewLine = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")});
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_New_Line"),seriesNewLine);
        JPanel ganntStylePane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Pattern"), panel);
        return ganntStylePane;
    }

    private JPanel createLinkLinePane(){
        lineWidth = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        colorSelect = new ColorSelectBoxWithOutTransparent(100);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] col = {f, e};
        double[] row = {p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Style")), lineWidth},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color")), colorSelect}
        };

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        JPanel linkLinePane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Link_Line"), panel);
        return linkLinePane;
    }

    //标记点类型
    protected JPanel createMarkerPane() {
        markerPane = new VanChartMarkerPane(){
            @Override
            protected VanChartCommonMarkerPane createCommonMarkerPane() {
                return new VanChartGanttCommonMarkerPane();
            }

            @Override
            protected BasicBeanPane<VanChartAttrMarker> createImageMarkerPane() {
                return new VanChartImageMarkerWithoutWidthAndHeightPane();
            }
        };
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gannt_Marker"), markerPane);
    }

    @Override
    public void populateBean(Plot plot) {
        super.populateBean(plot);

        if(plot instanceof VanChartGanttPlot){
            VanChartGanttPlot ganttPlot = (VanChartGanttPlot)plot;

            seriesNewLine.setSelectedIndex(ganttPlot.isSeriesNewLineEnable() ? 0 : 1);

            lineWidth.setSelectedLineStyle(ganttPlot.getLineWidth());
            colorSelect.setSelectObject(ganttPlot.getLineColor());

        }
    }

    @Override
    public void updateBean(Plot plot) {
        super.updateBean(plot);
        if(plot instanceof VanChartGanttPlot){
            VanChartGanttPlot ganttPlot = (VanChartGanttPlot)plot;

            ganttPlot.setSeriesNewLineEnable(seriesNewLine.getSelectedIndex() == 0);
            ganttPlot.setLineWidth(lineWidth.getSelectedLineStyle());
            ganttPlot.setLineColor(colorSelect.getSelectObject());

        }
    }

    @Override
    protected VanChartBeautyPane createStylePane() {
        return null;
    }
}
